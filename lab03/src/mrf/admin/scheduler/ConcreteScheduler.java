package mrf.admin.scheduler;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import mrf.dfs.DFSMaster;

public class ConcreteScheduler implements Scheduler{

	//Needs to know where files are and how many tasks each node is running;
	
	private DFSMaster 				 fileSys;
	private Map<String, Set<String>> tasks;
	private Map<String, Integer>	 maxLoad;
	
	public ConcreteScheduler(DFSMaster fileSys){
		this.fileSys = fileSys;
		this.tasks   = new ConcurrentHashMap<String, Set<String>>();
	}
	
	@Override
	public String scheduleMapTask(String taskName, String file) {
		try {
			Set<String> fileAvail = fileSys.lookupFile(file);
			int    minLoad = -1;
			String minNode = "";
			
			for(String n : fileAvail){
				if(tasks.get(n).size() < minLoad || minLoad < 0){
					minLoad = tasks.get(n).size();
					minNode = n;
				}
			}
			
			if(maxLoad.get(minNode) > minLoad) return minNode;
			
			minLoad = -1;
			minNode = "";
			
			for(String n : tasks.keySet()){
				if(tasks.get(n).size() < minLoad || minLoad < 0){
					minLoad = tasks.get(n).size();
					minNode = n;
				}
			}
			
			return minNode;
		} catch (RemoteException e) {
			//no guarantees if master fails
			e.printStackTrace();
			return null;
		} 
	}

	@Override
	public String scheduleReduceTask(String taskName) {
		int    minLoad = -1;
		String minNode = "";
		
		for(String n : tasks.keySet()){
			if(tasks.get(n).size() < minLoad || minLoad < 0){
				minLoad = tasks.get(n).size();
				minNode = n;
			}
		}
		
		return minNode;
	}

	@Override
	public void completeTask(String nodeName, String taskName) {
		tasks.get(nodeName).remove(taskName);
	}

	@Override
	public void addNode(String nodeName, int maxLoad) {
		this.tasks.put(nodeName, Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>()));
		this.maxLoad.put(nodeName, maxLoad);
	}
	
	@Override
	public void setDFS(DFSMaster dfs) {
		fileSys = dfs;
	}

}
