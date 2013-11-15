package mrf.taskengine.worker;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import mrf.tasks.MapCallable;
import mrf.tasks.ReduceCallable;

/**
 * Interface for a worker node
 * 
 * @author Michael Wang - mhw1
 *
 */
public interface TaskWorker extends Remote, Serializable {
	
	/**
	 * Runs a map task on the node
	 * @param t - task to run
	 * @param inPath - name of file to extract data from
	 * @param name - name of the task
	 * @throws RemoteException
	 */
	public <U, T> void runMapTask(MapCallable<U, T> t, String inPath, String name) throws RemoteException;
	
	/**
	 * Runs a reduce task on the node
	 * @param t - task to run
	 * @param inPaths - Workers to retrieve reduce data from
	 * @param name - name of the task
	 * @throws RemoteException
	 */
	public <T> void runReduceTask(ReduceCallable<T> t, ArrayList<TaskWorker> inPaths, String name) throws RemoteException;

	/**
	 * Returns data needed to reduce
	 * @param name - name of the task
	 * @param obj - type of result
	 * @return result of map task
	 * @throws RemoteException
	 */
	public <T> ArrayList<T> getReduceResults(String name, T obj) throws RemoteException;
	
	/**
	 * Returns the max load for this node
	 * @return
	 * @throws RemoteException
	 */
	public int getMaxLoad() throws RemoteException;
	
	/**
	 * Gets the name of this node
	 * @return
	 * @throws RemoteException
	 */
	public String getName() throws RemoteException;
}
