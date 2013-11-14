package mrf.taskengine.master;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import mrf.taskengine.worker.TaskWorker;
import mrf.tasks.MapReduceTask;

public interface TaskMaster extends Remote {
	public void doneMapping(String taskName) throws RemoteException;
	
	public <T> void doneReducing(String taskName, T result) throws RemoteException;
	
	public List<String> reduceData(String taskName) throws RemoteException;
	
	public <T> void addTask(MapReduceTask<T> task) throws RemoteException;
	
	public boolean addNode(TaskWorker worker) throws RemoteException;
	
	public void poll();
}
