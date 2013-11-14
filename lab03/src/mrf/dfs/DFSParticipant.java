package mrf.dfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import java.util.HashSet;

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

	/*
	 * TODO: Write File to local-space AND send to coordinator for replication
	 * 
	 * TODO: Read file from local-space
	 * 
	 * TODO: Get an ORIGINAL local copy from the coordinator
	 * 
	 * TODO: Return a TEMP copy to the coordinator
	 * 
	 * TODO: Return list of ORIGINAL replicas that it has access to.
	 */
	
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
		// TODO Auto-generated method stub
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
	
	public static void main (String[] args) {
		int port = (args.length < 1) ? 15150 : Integer.parseInt(args[0]);
		try {
			ServerSocket s = new ServerSocket(port);
			Socket soc = s.accept();
			
			ObjectInputStream ois = new ObjectInputStream(soc.getInputStream());
			ConfigInfo ci = (ConfigInfo) ois.readObject();
			
			// Do everything I need to before I send my ACK.
			String host = ci.getHost();
			String name = ci.getName();
			int hPort = ci.getPort();
			
			DFSParticipant DFS = new DFSParticipant(name, host, hPort);
			DFSNode stub = (DFSNode) UnicastRemoteObject.exportObject(DFS, 0);
			Registry registry = LocateRegistry.getRegistry(host);
			registry.bind(name, stub);
			
			ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
			oos.writeObject(ci);
			ois.close();
			oos.close();
			soc.close();
			
			
		} catch (Exception e) {
			System.err.println("Client exception: " + e);
		}
	}
}
