package mrf.taskengine.master;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import mrf.admin.scheduler.ConcreteScheduler;
import mrf.admin.scheduler.Scheduler;
import mrf.dfs.DFSMaster;
import mrf.dfs.MRFile;
import mrf.taskengine.worker.TaskWorker;
import mrf.tasks.MapReduceTask;

public class ServerNode implements TaskMaster{

	private static Scheduler sched = new ConcreteScheduler(null);
	private static DFSMaster dfs   = null;
	private static Map<String, TaskWorker>    workers = new ConcurrentHashMap<String, TaskWorker>();
	//map from task names to node names
	private static Map<String, String>        taskMap = new ConcurrentHashMap<String, String>();
	//map from task names to map node dependencies
	private static Map<String, List<String>>  dataMap = new ConcurrentHashMap<String, List<String>>();
	//map from task names to completed map nodes
	private static Map<String, Set<String>>   dataRcv = new ConcurrentHashMap<String, Set<String>>();
	//map from task names to tasks
	private static Map<String, MapReduceTask<?, ?>> taskLst = new ConcurrentHashMap<String, MapReduceTask<?, ?>>();  
	
	public ServerNode(DFSMaster dfs){
		ServerNode.dfs = dfs;
		sched.setDFS(dfs);
	}
	
	@Override
	public void doneMapping(String taskName) throws RemoteException {
		sched.completeTask(taskMap.get(taskName), taskName);
		String[] taskID = taskName.split(":");
		String node = dataMap.get(taskID[0]).get(Integer.parseInt(taskID[1]));
		dataRcv.get(taskID[0]).add(node);
		//if all map nodes completed, reduce
		if(dataRcv.get(taskID[0]).size() == dataMap.get(taskID[0]).size()){
			String redNode = sched.scheduleReduceTask(taskID[0]);
			ArrayList<TaskWorker> parts = new ArrayList<TaskWorker>();
			for(String s : dataMap.get(taskID[0])){
				parts.add(workers.get(s));
			}
			workers.get(redNode).runReduceTask(taskLst.get(taskName).getReduceTask(), parts, taskID[0]);
		}
	}

	@Override
	public <T> void doneReducing(String taskName, T result)
			throws RemoteException {
		sched.completeTask(taskMap.get(taskName), taskName);
		ArrayList<Object> resultList = new ArrayList<Object>();
		resultList.add(result);
		dfs.distributeFile(new MRFile(resultList, taskName));
	}

	@Override
	public List<String> reduceData(String taskName) throws RemoteException {
		return dataMap.get(taskName);
	}

	@Override
	public <U, T> void addTask(MapReduceTask<U, T> task) throws RemoteException {
		taskLst.put(task.getName(), task);
		dataMap.put(task.getName(), new ArrayList<String>());
		dataRcv.put(task.getName(), new HashSet<String>());
		
		for(int i = 0; i < task.getFiles().size(); i++){
			String node = sched.scheduleMapTask(task.getName() + ":" + i, task.getFiles().get(i));
			taskMap.put(task.getName() + ":" + i, node);
			dataMap.get(task.getName()).add(task.getFiles().get(i));
			workers.get(node).runMapTask(task.getMapTask(), task.getFiles().get(i), task.getName() + ":" + i);
		}
	}

	@Override
	public boolean addNode(TaskWorker worker) throws RemoteException {
		if(workers.containsKey(worker.getName())) return false;
		workers.put(worker.getName(), worker);
		sched.addNode(worker.getName(), worker.getMaxLoad());
		return true;
	}
	
	@Override
	public void poll() {
		// TODO Auto-generated method stub
		
	}

}
