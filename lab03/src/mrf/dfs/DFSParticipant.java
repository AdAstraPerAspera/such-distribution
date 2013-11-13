package mrf.dfs;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class DFSParticipant implements DFSNode {
	String name;
	String coordinator;
	int hPort;
	
	public DFSParticipant (String name, String coordinator, int port) {
		this.name = name;
		this.coordinator = coordinator;
		this.hPort = port;
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
	
	public void writeFile (File f) {
		
	}

	@Override
	public File getFile(String name) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
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
