package mrf.taskengine.master;

import java.util.ArrayList;

import mrf.taskengine.worker.RemoteMapReduceWorker;

/**
 * An interface for a master node, with functions to manage compute nodes.
 * 
 * Any instance of this should take a MasterSettings object in its constructor.
 * 
 * There should only ever be one instance of this running at any given time.
 * 
 * @author Michael Wang - mhw1
 */
public interface MapReduceMaster {
	/**
	 * Adds a task to the master queue.
	 * 
	 * Can also be called on a worker.
	 * @return success of operation
	 */
	public boolean addTask();
	
	/**
	 * Polls all threads to determine which if any are dead
	 * @return list of dead threads
	 */
	public ArrayList<RemoteMapReduceWorker> poll();
	
	/**
	 * Adds a compute node to the internal representation
	 * @param w the compute node to be added
	 * @return success of operation
	 */
	public boolean addWorker(RemoteMapReduceWorker w); 
	
	
}
