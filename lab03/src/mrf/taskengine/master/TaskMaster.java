package mrf.taskengine.master;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import mrf.taskengine.worker.TaskWorker;
import mrf.tasks.MapReduceTask;

/**
 * Interface for a coordinator for worker nodes
 * 
 * @author Michael Wang - mhw1
 *
 */
public interface TaskMaster extends Remote {
	/**
	 * Performs cleanup after a map task completes
	 * @param taskName - name of the task that completed
	 * @throws RemoteException
	 */
	public void doneMapping(String taskName) throws RemoteException;
	
	/**
	 * Performs cleanup after a reduce task completes
	 * @param taskName - name of the task that completed
	 * @param result - value of the computation
	 * @throws RemoteException
	 */
	public <T> void doneReducing(String taskName, T result) throws RemoteException;
	
	/**
	 * Returns a list of names of nodes that performed map tasks for a computation 
	 * @param taskName - name of task
	 * @return - list of names
	 * @throws RemoteException
	 */
	public List<String> reduceData(String taskName) throws RemoteException;
	
	/**
	 * Adds a task to be executed
	 * @param task
	 * @throws RemoteException
	 */
	public <U, T> void addTask(MapReduceTask<U, T> task) throws RemoteException;
	
	/**
	 * Adds a node
	 * @param worker
	 * @return
	 * @throws RemoteException
	 */
	public boolean addNode(TaskWorker worker) throws RemoteException;
	
	/**
	 * Returns a list of living nodes
	 * @return
	 * @throws RemoteException
	 */
	public List<String> poll() throws RemoteException;
}
