package mrf.taskengine.worker;

import java.rmi.RemoteException;
import java.util.ArrayList;

import mrf.dfs.DFSNode;
import mrf.taskengine.master.TaskMaster;
import mrf.tasks.MapCallable;
import mrf.tasks.ReduceCallable;

public class WorkerNode implements TaskWorker{
	private DFSNode	   node;
	private TaskMaster master;

	public WorkerNode(DFSNode node, TaskMaster master){
		this.node   = node;
		this.master = master;
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
		for(TaskWorker w : workers){
			ArrayList<T> wData = w.getReduceResults(name);
			for(T datum : wData){
				data.add(datum);
			}
		}
		
		T result = (new ReduceThread<T>(t, data)).run();
		master.doneReducing(name, result);
	}
	
	@Override
	public <T> T getReduceResults(String name){
		return null;
	}
}
