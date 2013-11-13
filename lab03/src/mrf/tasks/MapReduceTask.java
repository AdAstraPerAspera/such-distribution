package mrf.tasks;

import java.util.ArrayList;

public class MapReduceTask<T> {
	private MapCallable<T>    mapTask;
	private ReduceCallable<T> reduceTask;
	private ArrayList<String> files;
	
	public MapReduceTask(MapCallable<T> mapTask, ReduceCallable<T> reduceTask, ArrayList<String> files){
		this.mapTask    = mapTask;
		this.reduceTask = reduceTask;
		this.files      = files;
	}
	
	public MRTask<T> getMapTask(){
		return mapTask;
	}
	
	public MRTask<T> getReduceTask(){
		return reduceTask;
	}
	
	public ArrayList<String> getFiles(){
		return files;
	}
}
