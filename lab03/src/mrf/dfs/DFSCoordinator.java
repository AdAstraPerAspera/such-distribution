package mrf.dfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import mrf.admin.manager.ConfigData;

/**
 * The DFS Coordinator. Distributes files across the system and reports which participants have copies
 * of which files.
 * 
 * @author William Maynes - wmaynes
 *
 */

public class DFSCoordinator implements DFSMaster {

	private HashMap<String, HashSet<String>> part2file;
	private HashMap<String, HashSet<String>> file2part;
	private HashMap<String, String> part2loc;
	private int repfactor = 0;
	private int chunksize = 0;
	
	private static int partIndex;
	private ArrayList<String> partNames;
	
	public String url;
	public int port;
	public int regPort;
	
	/**
	 * Constructor for the DFS - Pulls necessary information out of cd and initializes other fields
	 * @param cd
	 * @throws Exception
	 */
	public DFSCoordinator (ConfigData cd) throws Exception {
		String initData = cd.getInitData();
		this.port = cd.getPort();
		this.regPort = cd.getRegPort();
		this.url = cd.getHost();
		this.repfactor = cd.getRep();
		this.chunksize = cd.getChunk();
		this.part2file = cd.getP2F();
		this.part2loc = cd.getP2L();
		this.file2part = new HashMap<String, HashSet<String>>();

		if (this.repfactor <= 0) {
			throw new Exception("Failed to parse a valid replication factor from the config file");
		}
		else {
			this.repfactor = (this.repfactor <= this.part2file.size()) ? this.repfactor : this.part2file.size();
		}
		
		this.partNames = new ArrayList<String>(this.part2loc.keySet());
		DFSCoordinator.partIndex = 0;
		
		for (String p : this.partNames) {
			String loc = part2loc.get(p);
			ConfigInfo ci = new ConfigInfo(p, url, port, cd.getRegPort());
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
	}
	
	public int portFromLoc(String str) {
		String[] s = str.split(":");
		return Integer.parseInt(s[1]);
	}
	public String hostFromLoc(String str) {
		String[] s = str.split(":");
		return s[0];
	}
	
	/**
	 * Partition a MRFile according to the chunksize
	 * @param mrf
	 * @return
	 */
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
	
	
	/**
	 * Convert a normal file into an MRFile assuming correct format
	 * @param f
	 * @return
	 * @throws Exception
	 */
	private MRFile F2MRFile (File f) throws Exception{
		MRFile mrf = null;
		if(f.isFile()) {
			try {
				FileInputStream fis = new FileInputStream(f);
				ObjectInputStream ois = new ObjectInputStream(fis);
				ArrayList<Object> A = (ArrayList<Object>) ois.readObject();
				ois.close();
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
	/**
	 * Distribute a MRFile across the network.
	 */
	public void distributeFile(MRFile mrf) throws RemoteException {
		if(mrf != null) {
			Registry reg = LocateRegistry.getRegistry(url, regPort);
			ArrayList<MRFile> A = partitionFile(mrf);
			for(int i = 0; i < A.size(); i++) {
				int repcount = 0;
				int startIndex = partIndex;
				while (repcount < this.repfactor) {
					String part = partNames.get(partIndex);
					String fName = mrf.getName();
					// Start by adding the file to the corresponding HashMaps so that they can be used later.
					HashSet<String> fs = part2file.get(part);
					HashSet<String> ps = file2part.get(fName + i);
					fs.add(fName + "-" + i);
					if(ps == null) ps = new HashSet<String>();
					ps.add(part);
					HashSet<String> oldfs = part2file.put(part, fs);
					HashSet<String> oldps = file2part.put(fName + "-" + i, ps);
					/* 
					 * Try to actually write the file to the node. If it works, move to the next replica, otherwise
					 * roll back the change and continue on the same replica count.
					 */
					try {
						DFSNode stub = (DFSNode) reg.lookup(part);
						stub.writeFile(A.get(i));
						repcount++;
						partIndex = (partIndex + 1) % partNames.size();
					} catch (NotBoundException e) {
						part2file.put(part, oldfs);
						file2part.put(fName + i, oldps);
						partIndex = (partIndex + 1) % partNames.size();
					}
					// If we have gone through ever possible participant, break the current file distribution
					if (partIndex == startIndex) {
						break;
					}
				}
			}
		}
	}

	@Override
	public Set<String> listFiles() throws RemoteException {
		return file2part.keySet();
	}
}
