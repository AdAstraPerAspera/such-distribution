package mrf.admin.manager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import mrf.config.ConfigParser;
import mrf.dfs.DFSCoordinator;
import mrf.dfs.DFSMaster;
import mrf.taskengine.master.ServerNode;
import mrf.taskengine.master.TaskMaster;

public class MasterServer {
	
	private static int portFromLoc(String str) {
		String[] s = str.split(":");
		return Integer.parseInt(s[1]);
	}
	
	private static String hostFromLoc(String str) {
		String[] s = str.split(":");
		return s[0];
	}

	private static ConfigData readConfig(String path) throws Exception {
		String initData = null;
		int repfactor = 0;
		int chunksize = 1;
		String host = null;
		int port = 0;
		int port2 = 0;
		HashMap<String, HashSet<String>> part2file = new HashMap<String, HashSet<String>>();
		//HashMap<String, HashSet<String>> file2part = new HashMap<String, HashSet<String>>();
		HashMap<String, String> part2loc  = new HashMap<String, String>();
		try {
			FileInputStream fis = new FileInputStream(path);
			HashMap<String, String> parsed = ConfigParser.parse(fis);
			fis.close();
			
			for(String s: parsed.keySet()) {
				if(s.equals("factor")) {
					repfactor = Integer.parseInt(parsed.get(s));
				} else if (s.equals("chunksize")) {
					chunksize = Integer.parseInt(parsed.get(s));
				} else if (s.equals("initdata")) {
					initData = parsed.get(s);
				} else if (s.equals("dfsmaster")) {
					String loc = parsed.get(s);
					host = hostFromLoc(loc);
					port = portFromLoc(loc);
				} else if (s.equals("registry")) {
					String loc = parsed.get(s);
					port2 = portFromLoc(loc);
				} else {
					HashSet<String> files = new HashSet<String>();
					String tmploc = parsed.get(s);
					part2file.put(s, files);
					part2loc.put(s, tmploc);
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("Invalid Config File Path: " + e);
		}
		return new ConfigData(host, port, port2, repfactor, chunksize, part2file, part2loc, initData);
	}
	
	private static DFSMaster startDFS(Registry registry, String config) throws Exception{
		ConfigData cd = readConfig(config);
		registry = LocateRegistry.createRegistry(cd.getRegPort());
		DFSCoordinator dfs = new DFSCoordinator(cd);
		DFSMaster stub = (DFSMaster) UnicastRemoteObject.exportObject(dfs, 0);
		registry.bind("dfsmaster", stub);
		return dfs;
	}
	
	private static TaskMaster startServer(DFSMaster dfs, Registry registry) throws Exception{
		TaskMaster taskServer = new ServerNode(dfs);
		TaskMaster serverStub = (TaskMaster) UnicastRemoteObject.exportObject(taskServer, 0);
		registry.bind("taskmaster", serverStub);
		return taskServer;
	}
	
	public static void main (String[] args) throws Exception {
		Scanner cin = new Scanner(System.in);
		Registry registry = null;
		
		System.out.println("Starting master node...");
		System.out.print("Path to config file: ");
		String config = cin.nextLine();
		
		DFSMaster dfsMaster = MasterServer.startDFS(registry, config);
		
		ConfigData cd = readConfig(config);
		registry = LocateRegistry.getRegistry("localhost", cd.getRegPort());
		TaskMaster master = MasterServer.startServer(dfsMaster, registry);
		
		while(true){
			String input = cin.nextLine();
			switch(input.toLowerCase()){
			case "status":
			case "poll":
				System.out.println("Live mapreduce nodes: ");
				List<String> avail = master.poll();
				for(String s : avail){
					System.out.print(s + " ");
				}
				System.out.println();
				break;
			default:
				System.out.println("Options are:\npoll (displays a list of living nodes)");
			}
		}
	}
}
