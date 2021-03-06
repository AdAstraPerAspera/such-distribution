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
import mrf.dfs.DFSMaster;
import mrf.dfs.DFSNode;
import mrf.dfs.DFSParticipant;
import mrf.taskengine.master.TaskMaster;
import mrf.taskengine.worker.TaskWorker;
import mrf.taskengine.worker.WorkerNode;

/**
 * Main class to run a DFS node and an associated compute node
 * 
 * @author Michael Wang - mhw1
 * @author William Maynes - wmaynes
 *
 */
public class WorkerServer {
	private static String reghost;
	private static int regport;
	
	private static DFSNode startDFSNode(int port) throws Exception{
		ServerSocket s = new ServerSocket(port);
		Socket soc = s.accept();
		
		ObjectInputStream ois = new ObjectInputStream(soc.getInputStream());
		ConfigInfo ci = (ConfigInfo) ois.readObject();
		
		// Do everything I need to before I send my ACK.
		String host = ci.getHost();
		reghost = ci.getHost();
		String name = ci.getName();
		int hPort = ci.getRegPort();
		regport = ci.getRegPort();
		
		DFSParticipant DFS = new DFSParticipant(name, host, hPort);
		DFSNode stub = (DFSNode) UnicastRemoteObject.exportObject(DFS, 0);
		Registry registry = LocateRegistry.getRegistry(host, hPort);
		System.out.println("Registry: " + hPort);
		registry.bind(name, stub);
		
		ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
		oos.writeObject(ci);
		ois.close();
		oos.close();
		soc.close();
		
		s.close();
		
		return DFS;
	}
	
	private static TaskWorker startMapReduceNode(String name, Registry registry, DFSNode node, int port) throws Exception{
		TaskMaster master = (TaskMaster) registry.lookup("taskmaster");
		WorkerNode worker = new WorkerNode(name, node, master);
		return worker;
	}
	
	public static void main(String[] args) throws Exception{
		Scanner cin = new Scanner(System.in);
		
		System.out.println("Starting worker node...");
		System.out.print("Port to listen on? ");
		int port = cin.nextInt();
		String name = cin.nextLine();
		
		DFSNode node = WorkerServer.startDFSNode(port);
		
		System.out.println("Name of task node? ");
		while(name.equals("") || name.equals("\n") || name == null) name = cin.nextLine();
		
		Registry registry = LocateRegistry.getRegistry(reghost, regport);
		TaskMaster master = null;
		
		while(true){
			try {
				master = (TaskMaster) registry.lookup("taskmaster");
			} catch (Exception e) {
				continue;
			}
			break;
		}
		TaskWorker worker = startMapReduceNode(name, registry, node, port);
		
		registry.bind(name, worker);
		TaskWorker workerStub = (TaskWorker) UnicastRemoteObject.exportObject(worker, 0);
		
		master.addNode(workerStub);
	}
}
