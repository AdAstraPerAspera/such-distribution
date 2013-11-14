package mrf.admin.manager;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import mrf.dfs.ConfigInfo;
import mrf.dfs.DFSNode;
import mrf.dfs.DFSParticipant;

public class WorkerServer {
	public static void main(String[] args){
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
