package mrf.dfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
	private int repfactor;
	private int chunksize;
	
	private static int partIndex;
	private String[] partNames;
	
	private String url;
	private int port;
	
	
	/* 
	 * TODO: Replicate files among participants
	 * TODO: Track Replication
	 * 
	 * TODO: Send a copy of a file from one node to another if requested
	 * 
	 * TODO: Threads
	 */
	
	protected int portFromLoc(String str) {
		String[] s = str.split(":");
		return Integer.parseInt(s[1]);
	}
	protected String hostFromLoc(String str) {
		String[] s = str.split(":");
		return s[0];
	}
	
	private ArrayList<MRFile> partitionFile (ArrayList<Object> A, String name) {
		int c = chunksize;
		int partCount = (A.size() % c == 0) ? A.size() / c : (A.size() / c) + 1;
		ArrayList<MRFile> newFiles = new ArrayList<MRFile>(partCount);
		Object[] arr = A.toArray();
		for (int i = 0; i < partCount; i++) {
			ArrayList<Object> newChunk = new ArrayList<Object>(Arrays.asList(Arrays.copyOfRange(arr, i*c, (i+1)*c)));
			newFiles.add(new MRFile(newChunk, name + "-" + i));
		}
		return newFiles;
	}
	
	public DFSCoordinator (String host, String configPath, int port) throws Exception {
		this.url = host;
		this.port = port;
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
				/* 
				 * TODO: Distribute files here, use partNames and partIndex for even distribution
				 */
				for(int i = 0; i < this.repfactor; i++){
					// For the multiple copies
					continue;
				}
			}

			
		} catch (FileNotFoundException e) {
			System.err.println("Invalid Config File Path: " + e);
		} catch (IOException e) {
			System.err.println("IOException while parsing config: " + e);
		} catch (Exception e) {
			System.err.println("Exception: " + e);
		}
	}


	@Override
	public HashSet<String> lookupFile(String name) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void distributeFile(File f) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	public static void main (String[] args) {
		// TODO: Change this to only take a config file - No Host address or Port, those should be parsed from config
		String host = (args.length < 1) ? null : args[0];
		int port = (args.length < 2) ? 15150 : Integer.parseInt(args[1]);
		String config = (args.length < 3) ? null : args[2];
		try {
			Registry registry = LocateRegistry.getRegistry(host);
			DFSCoordinator DFS = new DFSCoordinator(host, config, port);
			DFSMaster stub = (DFSMaster) UnicastRemoteObject.exportObject(DFS, 0);
			registry.bind("master", stub);
		} catch (Exception e) {
			System.err.println("Client exception: " + e);
		}
	}
	
}
