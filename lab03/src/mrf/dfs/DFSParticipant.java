package mrf.dfs;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;

/**
 * DFS Participant. Writes files to its local space, recording that it has a copy of said file. 
 * Also returns files that are requested, whether from local space or somewhere else on the system.
 * 
 * @author William Maynes - wmaynes
 *
 */

public class DFSParticipant implements DFSNode {
	private String name;
	private String coordinator;
	private int hPort;
	private HashSet<String> localFiles;
	
	public DFSParticipant (String name, String coordinator, int port) {
		this.name = name;
		this.coordinator = coordinator;
		this.hPort = port;
		this.localFiles = new HashSet<String>();
	}

	public void writeFile (MRFile mrf) {
		try {
			String fName = mrf.getName();
			FileOutputStream fos = new FileOutputStream(fName);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(mrf);
			oos.close();
			this.localFiles.add(fName);
		} catch (Exception e) {
			System.err.print("Failure writing file to node");
		}
	}

	@Override
	public MRFile getFile(String name) throws RemoteException {
		MRFile mrf = null;
		boolean hasFile = localFiles.contains(name);
		if (hasFile) {
			try {
				FileInputStream fis = new FileInputStream(name);
				ObjectInputStream ois = new ObjectInputStream(fis);
				mrf = (MRFile) ois.readObject();
				ois.close();
			} catch (Exception e) {
				System.err.println("Failed to read file: " + name + ", threw: " + e);
			}
		}
		else {
			try {
				Registry reg = LocateRegistry.getRegistry(coordinator, hPort);
				DFSCoordinator dfs = (DFSCoordinator) reg.lookup("dfsmaster");
				HashSet<String> nodes = dfs.lookupFile(name);
				for (String s : nodes) {
					try {
						DFSParticipant stub = (DFSParticipant) reg.lookup(s);
						mrf = stub.getFile(name);
						break;
					} catch (Exception e) {
						continue;
					}
				}
			} catch (NotBoundException e) {
				System.err.println("Participant or Coordinator not bound: " + e);
			}
		}
		writeFile(mrf);
		return mrf;
	}
}
