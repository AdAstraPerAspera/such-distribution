package mrf.admin.scheduler;

import java.util.*;

public interface Scheduler {
	/**
	 * Adds a new worker node
	 * @param s
	 * @return
	 */
	public boolean addNode(String s);
	
	/**
	 * Removes a worker node
	 * @param s
	 * @return set of tasks that need to be reassigned
	 */
	public Set<String> removeNode(String s);
	
	/**
	 * Schedules a task to a node
	 * @param task
	 * @return
	 */
	public String schedule(String task);
	
	/**
	 * Schedules a set of tasks to nodes
	 * @param task
	 * @return
	 */
	public HashMap<String, Set<String>> schedule(Set<String> task);
	
	/**
	 * Reschedules a failed task
	 * @param task
	 * @return
	 */
	public String fail(String task);
	
	/**
	 * Reschedules a set of failed tasks
	 * @param task
	 * @return
	 */
	public HashMap<String, Set<String>> fail(Set<String> task);
	
	/**
	 * Removes a completed task
	 * @param task
	 */
	public void complete(String task);
	
	/**
	 * Removes a set of completed tasks
	 * @param task
	 */
	public void complete(Set<String> task);
}
