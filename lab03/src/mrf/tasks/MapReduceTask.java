package mrf.tasks;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class MapReduceTask<T> implements Serializable{
	private String            name;
	private MapCallable<T>    mapTask;
	private ReduceCallable<T> reduceTask;
	private ArrayList<String> files;
	
	public MapReduceTask(String name, MapCallable<T> mapTask, ReduceCallable<T> reduceTask, ArrayList<String> files){
		this.mapTask    = mapTask;
		this.reduceTask = reduceTask;
		this.files      = files;
	}
	
	public MapCallable<T> getMapTask(){
		return mapTask;
	}
	
	public ReduceCallable<T> getReduceTask(){
		return reduceTask;
	}
	
	public ArrayList<String> getFiles(){
		return files;
	}
	
	public String getName(){
		return name;
	}
}
