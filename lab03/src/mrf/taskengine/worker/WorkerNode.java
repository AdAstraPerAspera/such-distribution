package mrf.taskengine.worker;

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
	public <T> void runMapTask(MapCallable<T> t, String inPath, String name)
			throws RemoteException {
		(new MapThread<T>(node, t, inPath, name)).run();
		master.doneMapping(name);
	}

	@Override
	public <T> void runReduceTask(ReduceCallable<T> t, ArrayList<TaskWorker> workers, String name)
			throws RemoteException {
		ArrayList<T> data = new ArrayList<T>();
		int i = 0;
		for(TaskWorker w : workers){
			ArrayList<T> wData = w.getReduceResults(name + ":" + i);
			for(T datum : wData){
				data.add(datum);
			}
			i++;
		}
		
		T result = (new ReduceThread<T>(t, data)).run();
		master.doneReducing(name, result);
	}
	
	@Override
	public <T> ArrayList<T> getReduceResults(String name) throws RemoteException{
		return (ArrayList<T>) node.getFile(name).getContents();
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
