package mrf.admin.scheduler;

import mrf.dfs.DFSMaster;

/**
 * Interface for a simple task scheduler
 * @author Michael Wang - mhw1
 *
 */
public interface Scheduler {
	/**
	 * Schedules a Mapping task to a node, giving preference to
	 * nodes which have the file required
	 * @param taskName - name of the task to schedule
	 * @param file - name of required file
	 * @return name of node to schedule task to
	 */
	public String scheduleMapTask(String taskName, String file);
	
	/**
	 * Schedules a reduce task to a node.
	 * @param taskName - name of the task to schedule
	 * @return name of node to schedule task to
	 */
	public String scheduleReduceTask(String taskName);
	
	/**
	 * Remove a task from the scheduler
	 * @param nodeName - name of the node running the task
	 * @param taskName - name of the completed task
	 */
	public void completeTask(String nodeName, String taskName);
	
	/**
	 * Adds a new compute node to the internal model
	 * @param nodeName
	 * @param maxLoad
	 */
	public void addNode(String nodeName, int maxLoad);
	
	/**
	 * Adds a reference to the DFS
	 * @param dfs
	 */
	public void setDFS(DFSMaster dfs);
}
