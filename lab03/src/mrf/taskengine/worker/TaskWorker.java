package mrf.taskengine.worker;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import mrf.tasks.MapCallable;
import mrf.tasks.ReduceCallable;

public interface TaskWorker extends Remote, Serializable {
	
	public <U, T> void runMapTask(MapCallable<U, T> t, String inPath, String name) throws RemoteException;
	
	public <T> void runReduceTask(ReduceCallable<T> t, ArrayList<TaskWorker> inPaths, String name) throws RemoteException;

	public <T> ArrayList<T> getReduceResults(String name, T obj) throws RemoteException;
	
	public int getMaxLoad() throws RemoteException;
	
	public String getName() throws RemoteException;
}
