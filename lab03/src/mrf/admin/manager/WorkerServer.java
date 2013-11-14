package mrf.admin.manager;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import mrf.dfs.ConfigInfo;
import mrf.dfs.DFSNode;
import mrf.dfs.DFSParticipant;

public class WorkerServer {
	private static void startDFSNode(int port) throws Exception{
		ServerSocket s = new ServerSocket(port);
		Socket soc = s.accept();
		
		System.out.println("Inbound connection recieved.");
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
		
		s.close();
	}
	
	public static void main(String[] args) throws Exception{
		Scanner cin = new Scanner(System.in);
		
		System.out.println("Starting worker node...");
		System.out.print("Port to listen on? ");
		int port = cin.nextInt();
		
		WorkerServer.startDFSNode(port);
		
		
	}
}
