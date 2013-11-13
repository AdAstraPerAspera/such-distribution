package mrf.admin.scheduler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConcreteScheduler implements Scheduler{
	private ConcurrentHashMap<String, Integer> load;
	private ConcurrentHashMap<String, Set<String>> asgn;
	
	/**
	 * Must have at least one node
	 * @param nodes
	 */
	public ConcreteScheduler(Set<String> nodes){
		this.load = new ConcurrentHashMap<String, Integer>();
		this.asgn = new ConcurrentHashMap<String, Set<String>>();
		for (String s : nodes){
			load.put(s, 0);
			asgn.put(s, new HashSet<String>());
		}
	}

	@Override
	public boolean addNode(String node){
		if(load.containsValue(node)) return false;
		load.put(node, 0);
		asgn.put(node, new HashSet<String>());
		return true;
	}
	
	@Override
	public Set<String> removeNode(String node){
		load.remove(node);
		return asgn.remove(node);
	}
	
	@Override
	public String schedule(String task) {
		int    minVal = -1;
		String minKey = "";
		
		for (String k : load.keySet()){
			if (load.get(k) > minVal || minVal < 0){
				minVal = load.get(k);
				minKey = k;
			}
		}
		
		load.put(minKey, load.get(minVal) + 1);
		asgn.get(minKey).add(task);
		
		return minKey;
	}

	@Override
	public HashMap<String, Set<String>> schedule(Set<String> task) {
		HashMap<String, Set<String>> retVal = new HashMap<String, Set<String>>();
		
		for(String s : task){
			String result = schedule(s);
			if(!retVal.containsKey(result)){
				retVal.put(result, new HashSet<String>());
			}
			retVal.get(result).add(s);
		}
		
		return retVal;
	}

	@Override
	public String fail(String task) {
		for(String k : asgn.keySet()){
			if(asgn.get(k).contains(task)){
				load.put(k, load.get(k) - 1);
				asgn.get(k).remove(task);
			}
		}
		
		return schedule(task);
	}

	@Override
	public HashMap<String, Set<String>> fail(Set<String> task) {
		HashMap<String, Set<String>> retVal = new HashMap<String, Set<String>>();
		
		for(String s : task){
			String result = fail(s);
			if(!retVal.containsKey(result)){
				retVal.put(result, new HashSet<String>());
			}
			retVal.get(result).add(s);
		}
		
		return retVal;
	}

	@Override
	public void complete(String task) {
		for(String k : asgn.keySet()){
			if(asgn.get(k).contains(task)){
				load.put(k, load.get(k) - 1);
				asgn.get(k).remove(task);
			}
		}
	}

	@Override
	public void complete(Set<String> task) {
		for(String s : task){
			complete(s);
		}
	}
}
