package mrf.admin.manager;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import mrf.dfs.DFSCoordinator;
import mrf.dfs.DFSMaster;
import mrf.taskengine.master.ServerNode;
import mrf.taskengine.master.TaskMaster;

public class MasterServer {
	private static void startDFS(String config) throws Exception{
		Registry registry = LocateRegistry.createRegistry(dfs.port);
		DFSCoordinator dfs = new DFSCoordinator(config);
		DFSMaster stub = (DFSMaster) UnicastRemoteObject.exportObject(dfs, 0);
		registry.bind("dfsmaster", stub);
	}
	
	private static void startServer(DFSMaster dfs, Registry registry) throws Exception{
		TaskMaster taskServer = new ServerNode(dfs);
		TaskMaster serverStub = (TaskMaster) UnicastRemoteObject.exportObject(taskServer, 0);
		registry.bind("taskmaster", serverStub);
	}
	
	public static void main (String[] args) throws Exception {
		Scanner cin = new Scanner(System.in);
		
		System.out.println("Starting master node...");
		System.out.print("Path to config file: ");
		String config = cin.nextLine();
		
		System.out.println(config);
		
		MasterServer.startDFS(config);
	}
}
