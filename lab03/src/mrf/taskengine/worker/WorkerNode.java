package mrf.taskengine.worker;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;

import mrf.dfs.DFSNode;
import mrf.taskengine.master.TaskMaster;
import mrf.tasks.MapCallable;
import mrf.tasks.ReduceCallable;

public class WorkerNode implements TaskWorker{
	private String     name;
	private DFSNode	   node;
	private TaskMaster master;
	private int        maxLoad;

	public WorkerNode(String name, DFSNode node, TaskMaster master){
		this.name    = name;
		this.node    = node;
		this.master  = master;
		this.maxLoad = Runtime.getRuntime().availableProcessors();
	}
	
	@Override
	public <U, T> void runMapTask(MapCallable<U, T> t, String inPath, String name)
			throws RemoteException {
		(new MapThread<U, T>(node, t, inPath, "/tmp/" + this.name + "/" + name)).run();
		master.doneMapping(name);
	}

	@Override
	public <T> void runReduceTask(ReduceCallable<T> t, ArrayList<TaskWorker> workers, String name)
			throws RemoteException {
		ArrayList<T> data = new ArrayList<T>();
		int i = 0;
		for(TaskWorker w : workers){
			ArrayList<T> wData = w.getReduceResults(name + ":" + i, (T) null);
			for(T datum : wData){
				data.add(datum);
			}
			i++;
		}
		
		T result = (new ReduceThread<T>(t, data)).run();
		master.doneReducing(name, result);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> ArrayList<T> getReduceResults(String name, T obj) throws RemoteException{
		try {
			FileInputStream   temp = new FileInputStream("/tmp/" + this.name + "/" + name);
			ObjectInputStream  ois = new ObjectInputStream(temp);
			temp.close();
			return (ArrayList<T>) ois.readObject();
		} catch (Exception e) {
			// Should not reach here
			e.printStackTrace();
		}
		return null;
		
		//return (ArrayList<T>) node.getFile(name).getContents();
	}
	
	@Override
	public int getMaxLoad() throws RemoteException{
		return maxLoad;
	}
	
	@Override
	public String getName() throws RemoteException{
		return this.name;
	}
}
