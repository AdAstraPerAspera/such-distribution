package mrf.taskengine.master;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import mrf.admin.scheduler.Scheduler;
import mrf.admin.scheduler.ConcreteScheduler;
import mrf.taskengine.worker.ConcreteMapReduceWorker;
import mrf.taskengine.worker.MapReduceWorker;
import mrf.taskengine.worker.WorkerSettings;

public class MasterServerState {
	private MasterSettings  setting;
	private Set<String>     nodeNames;
	private Set<String>		taskNames;
	private Scheduler       scheduler;
	private MapReduceWorker worker;
	
	public MasterServerState(MasterSettings setting){
		this.setting   = setting;
		this.nodeNames = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
		this.taskNames = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
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
	
	public void addNode(String s){
		nodeNames.add(s);
	}
	
	public void removeNode(String s){
		nodeNames.remove(s);
	}
	
	public boolean addTask(String s){
		if(taskNames.contains(s)) return false;
		taskNames.add(s);
		return true;
	}
	
	public Scheduler getScheduler(){
		return scheduler;
	}
}
