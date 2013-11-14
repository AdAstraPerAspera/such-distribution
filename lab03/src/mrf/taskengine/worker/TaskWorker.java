package mrf.taskengine.worker;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import mrf.tasks.MapCallable;
import mrf.tasks.ReduceCallable;

public interface TaskWorker extends Remote {
	public <T> void runMapTask(MapCallable<T> t, String inPath, String name) throws RemoteException;
	
	public <T> void runReduceTask(ReduceCallable<T> t, ArrayList<TaskWorker> inPaths, String name) throws RemoteException;

	public <T> T getReduceResults(String name) throws RemoteException;
	
	public int getMaxLoad() throws RemoteException;
	
	public String getName() throws RemoteException;
}
