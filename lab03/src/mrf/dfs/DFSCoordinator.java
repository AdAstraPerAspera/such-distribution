package mrf.dfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;


import mrf.config.ConfigParser;

public class DFSCoordinator implements DFSMaster {

	private HashMap<String, HashSet<String>> part2file;
	private HashMap<String, HashSet<String>> file2part;
	private HashMap<String, String> part2loc;
	private HashMap<String, String> loc2part;
	private int repfactor = 0;
	private int chunksize = 0;
	
	private static int partIndex;
	private String[] partNames;
	
	private String url;
	private int port;
	
	private Registry reg;
	
	
	/* 
	 * TODO: Send a copy of a file from one node to another if requested
	 * 
	 * TODO: Threads
	 */
	
	public DFSCoordinator (String host, String configPath, int port, Registry registry) throws Exception {
		this.url = host;
		this.port = port;
		this.reg = registry;
		try {
			FileInputStream fis = new FileInputStream(configPath);
			HashMap<String, String> parsed = ConfigParser.parse(fis);
			fis.close();
			
			String initData = null;
			for(String s: parsed.keySet()) {
				if(s.equals("factor")) {
					this.repfactor = Integer.parseInt(parsed.get(s));
				} else if (s.equals("chunksize")) {
					this.chunksize = Integer.parseInt(parsed.get(s));
				} else if (s.equals("initData")) {
					initData = parsed.get(s);
				} else {
					HashSet<String> files = new HashSet<String>();
					String tmploc = parsed.get(s);
					this.part2file.put(s, files);
					this.part2loc.put(s, tmploc);
					this.loc2part.put(tmploc, s);
				}
			}
			
			if (this.repfactor <= 0) {
				throw new Exception("Failed to parse a valid replication factor from the config file");
			}
			else {
				this.repfactor = (this.repfactor <= this.part2file.size()) ? this.repfactor : this.part2file.size();
			}
			
			this.partNames = this.part2loc.keySet().toArray(new String[0]);
			this.partIndex = 0;
			
			for (String p : this.partNames) {
				String loc = part2loc.get(p);
				ConfigInfo ci = new ConfigInfo(p, host, port);
				Socket soc = new Socket(hostFromLoc(loc), portFromLoc(loc));
				ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
				oos.writeObject(ci);
				ObjectInputStream ois = new ObjectInputStream(soc.getInputStream());
				ois.readObject();
				oos.close();
				ois.close();
				soc.close();
			}	
			
			File data = new File(initData);
			File[] dataFiles = data.listFiles();
			for (File f: dataFiles) {
				distributeFile(F2MRFile(f));
			}

			
		} catch (FileNotFoundException e) {
			System.err.println("Invalid Config File Path: " + e);
		} catch (IOException e) {
			System.err.println("IOException while parsing config: " + e);
		} catch (Exception e) {
			System.err.println("Exception: " + e);
		}
	}
	
	public int portFromLoc(String str) {
		String[] s = str.split(":");
		return Integer.parseInt(s[1]);
	}
	public String hostFromLoc(String str) {
		String[] s = str.split(":");
		return s[0];
	}
	
	private ArrayList<MRFile> partitionFile (MRFile mrf) {
		String name = mrf.getName();
		int c = chunksize;
		int partCount = (mrf.size() % c == 0) ? mrf.size() / c : (mrf.size() / c) + 1;
		ArrayList<MRFile> newFiles = new ArrayList<MRFile>(partCount);
		Object[] arr = mrf.getContents().toArray();
		for (int i = 0; i < partCount; i++) {
			ArrayList<Object> newChunk = new ArrayList<Object>(Arrays.asList(Arrays.copyOfRange(arr, i*c, (i+1)*c)));
			newFiles.add(new MRFile(newChunk, name + "-" + i));
		}
		return newFiles;
	}
	
	private MRFile F2MRFile (File f) throws Exception{
		MRFile mrf = null;
		if(f.isFile()) {
			try {
				FileInputStream fis = new FileInputStream(f);
				ObjectInputStream ois = new ObjectInputStream(fis);
				ArrayList<Object> A = (ArrayList<Object>) ois.readObject();
				mrf = new MRFile(A, f.getName());
			} catch (Exception e) {
				throw new Exception ("Exception while parsing file: " + e);
			}
		}
		return mrf;
	}

	@Override
	public HashSet<String> lookupFile(String name) throws RemoteException {
		return file2part.get(name);
	}


	@Override
	public void distributeFile(MRFile mrf) throws RemoteException {
		if(mrf != null) {
			ArrayList<MRFile> A = partitionFile(mrf);
			for(int i = 0; i < A.size(); i++) {
				int repcount = 0;
				int startIndex = partIndex;
				boolean firstFileInst = true;
				while (repcount < this.repfactor && firstFileInst) {
					String part = partNames[partIndex];
					String fName = mrf.getName();
					// Check to see if we are trying to add the file to a node that already has it
					HashSet<String> fs = part2file.get(part);
					HashSet<String> ps = file2part.get(fName);
					firstFileInst = firstFileInst & fs.add(fName);
					firstFileInst = firstFileInst & ps.add(part);
					HashSet<String> oldfs = part2file.put(part, fs);
					HashSet<String> oldps = file2part.put(fName, ps);
					/* 
					 * If we have not, then this is our first time here, so write the file and increment the
					 * number of replications we have made.
					 */
					try {
						if(firstFileInst) {
							DFSParticipant stub = (DFSParticipant) reg.lookup(part);
							stub.writeFile(A.get(i));
							repcount++;
						}
						partIndex = (partIndex + 1) % partNames.length;
					} catch (NotBoundException e) {
						part2file.put(part, oldfs);
						file2part.put(fName, oldps);
						partIndex = (partIndex + 1) % partNames.length;
					}
					// If we have gone through ever possible participant, break the current file distribution
					if (partIndex == startIndex) {
						break;
					}
				}
			}
		}
	}
	
	public static void main (String[] args) {
		// TODO: Change this to only take a config file - No Host address or Port, those should be parsed from config
		String host = (args.length < 1) ? null : args[0];
		int port = (args.length < 2) ? 15150 : Integer.parseInt(args[1]);
		String config = (args.length < 3) ? null : args[2];
		try {
			Registry registry = LocateRegistry.getRegistry(host);
			DFSCoordinator DFS = new DFSCoordinator(host, config, port, registry);
			DFSMaster stub = (DFSMaster) UnicastRemoteObject.exportObject(DFS, 0);
			registry.bind("master", stub);
		} catch (Exception e) {
			System.err.println("Client exception: " + e);
		}
	}
	
}
