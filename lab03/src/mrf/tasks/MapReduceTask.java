package mrf.tasks;

import java.util.ArrayList;

public class MapReduceTask {
	private SerializableCallable mapTask;
	private SerializableCallable reduceTask;
	private ArrayList<String>    files;
	
	public MapReduceTask(SerializableCallable mapTask, SerializableCallable reduceTask, ArrayList<String> files){
		this.mapTask    = mapTask;
		this.reduceTask = reduceTask;
		this.files      = files;
	}
	
	public SerializableCallable getMapTask(){
		return mapTask;
	}
	
	public SerializableCallable getReduceTask(){
		return reduceTask;
	}
	
	public ArrayList<String> getFiles(){
		return files;
	}
}
