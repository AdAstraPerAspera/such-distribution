package mrf.taskengine.worker;

/**
 * An interface for a compute node class.
 * 
 * Any instance of this should take a WorkerSettings object in its constructor.
 * 
 * @author Michael Wang - mhw1
 */
public interface MapReduceWorker {
	
	/**
	 * Otherwise, adds a task to the master queue.
	 * 
	 * Can also be called on a master.
	 */
	public void addTask();
	
	/**
	 * Adds a task to the node's queue.
	 */
	public void queueTask();
	
}
