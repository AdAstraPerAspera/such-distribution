package mrf.admin.manager;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import mrf.dfs.DFSCoordinator;
import mrf.dfs.DFSMaster;
import mrf.taskengine.master.ServerNode;
import mrf.taskengine.master.TaskMaster;

public class MasterServer {
	public static void main (String[] args) {
		String config = (args.length < 1) ? null : args[0];
		try {
			DFSCoordinator dfs = new DFSCoordinator(config);
			Registry registry = LocateRegistry.createRegistry(dfs.port);
			DFSMaster stub = (DFSMaster) UnicastRemoteObject.exportObject(dfs, 0);
			registry.bind("dfsmaster", stub);
			
			/*TaskMaster taskServer = new ServerNode(dfs);
			TaskMaster serverStub = (TaskMaster) UnicastRemoteObject.exportObject(taskServer, 0);
			registry.bind("taskmaster", serverStub);*/
		} catch (Exception e) {
			
			System.err.println("Client exception: " + e);
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
