package mrf.taskengine.worker;

import java.util.HashMap;
import java.util.Set;

import mrf.taskengine.master.SerializableCallable;

/**
 * An interface for a compute node class.
 * 
 * Any instance of this should take a WorkerSettings object in its constructor.
 * 
 * @author Michael Wang - mhw1
 */
public interface MapReduceWorker {
	
	/**
	 * Adds a task to the master's queue.
	 * 
	 * Can also be called on a master node.
	 * 
	 * @param c - task to be added
	 * @return success of operation
	 */
	public boolean addTask(SerializableCallable c);
	
	/**
	 * Adds a task to the master's queue.
	 * 
	 * Can also be called on a master node.
	 * 
	 * @param s - user-provided identifier for the task
	 * @param c - task to be added
	 * @return success of operation
	 */
	public boolean addTask(String s, SerializableCallable c);
	
	/**
	 * Adds a task to the node's queue.
	 * 
	 * @return success of operation
	 */
	public boolean queueTask(String s, SerializableCallable c);
	
	/**
	 * Checks to see which, if any of the processes have returned successfully
	 * since the last check
	 * 
	 * @return HashMap of task identifiers to return values from successful tasks
	 */
	public HashMap<String, Object> yield();
	
	/**
	 * Checks to see if any tasks have failed since the last check.
	 * 
	 * @return Set of task identifiers of failed tasks
	 */
	public Set<String> poll();
}
