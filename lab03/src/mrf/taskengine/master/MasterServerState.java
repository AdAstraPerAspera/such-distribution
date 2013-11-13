package mrf.taskengine.master;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import mrf.admin.scheduler.Scheduler;
import mrf.admin.scheduler.ConcreteScheduler;
import mrf.taskengine.worker.ConcreteMapReduceWorker;
import mrf.taskengine.worker.MapReduceWorker;
import mrf.taskengine.worker.NodeInformation;
import mrf.taskengine.worker.WorkerSettings;
import mrf.tasks.MapReduceTask;
import mrf.tasks.SerializableCallable;

public class MasterServerState {
	private MasterSettings				 setting;
	private Map<String, NodeInformation> nodeNames;
	private Map<String, MapReduceTask>	 taskNames;
	private Map<String, Set<String>>	 mapTasks;
	private Scheduler					 scheduler;
	private MapReduceWorker				 worker;
	
	public MasterServerState(MasterSettings setting){
		this.setting   = setting;
		this.nodeNames = new ConcurrentHashMap<String, NodeInformation>();
		this.taskNames = new ConcurrentHashMap<String, MapReduceTask>();
		this.mapTasks  = new ConcurrentHashMap<String, Set<String>>();
		this.scheduler = new ConcreteScheduler(new HashSet<String>());
		scheduler.addNode("master");
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("masterhost", "localhost");
		map.put("masterport", "" + setting.getMasterPort());
		this.worker = new ConcreteMapReduceWorker(new WorkerSettings(map));
	}
	
	public String getSetting(String s){
		return setting.getValue(s);
	}
	
	public void addNode(String s, NodeInformation i){
		nodeNames.put(s, i);
	}
	
	public void removeNode(String s){
		nodeNames.remove(s);
	}
	
	public boolean hasTask(String s){
		return taskNames.keySet().contains(s);
	}
	
	public boolean addTask(String s, MapReduceTask t){
		if(taskNames.keySet().contains(s)) return false;
		taskNames.put(s, t);
		return true;
	}
	
	public boolean addMapTask(String t, String m){
		if(!mapTasks.containsKey(t)){
			mapTasks.put(t, Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>()));
		}
		
		return mapTasks.get(t).add(m);
	}
	
	public boolean removeMapTask(String t, String m){
		return mapTasks.get(t).remove(m);
	}
	
	public Scheduler getScheduler(){
		return scheduler;
	}
	
	public boolean runTask(String n, SerializableCallable t, String p){
		return worker.queueTask(n, t, p);
	}
	
	public NodeInformation getNodeInfo(String s){
		return nodeNames.get(s);
	}
}
