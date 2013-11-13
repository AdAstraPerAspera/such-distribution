package mrf.admin.scheduler;

public interface Scheduler {
	/**
	 * TODO: Complete Scheduler
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
	
	public String scheduleReduceTask(String taskName);
	
	public void completeTask(String nodeName, String taskName);
}
