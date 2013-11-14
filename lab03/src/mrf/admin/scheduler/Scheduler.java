package mrf.admin.scheduler;

import mrf.dfs.DFSMaster;

public interface Scheduler {
	/**
	 * TODO: Complete Scheduler (pretty much done)
	 * 
	 * TODO: Concrete implementation of MasterServer
	 * 
	 * TODO: Main files for Master and Worker
	 * 
	 * TODO: Complete integration with DFS
	 * 
	 * TODO: Complete management stuff
	 * 
	 * 
	 * TODO: Examples
	 */
	
	//to reschedule, call completeTask and one of the scheduling functions
	
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
	
	public void setDFS(DFSMaster dfs);
}
