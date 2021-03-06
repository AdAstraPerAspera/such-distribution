package mrf.tasks;

import java.util.ArrayList;

/**
 * Concrete implementation for MRTask
 * 
 * @author Michael Wang - mhw1
 *
 * @param <U>
 * @param <T>
 */
@SuppressWarnings("serial")
public class MapReduceTask<U, T> implements MRTask<U, T>{
	private String            name;
	private MapCallable<U, T> mapTask;
	private ReduceCallable<T> reduceTask;
	private ArrayList<String> files;
	
	public MapReduceTask(String name, MapCallable<U, T> mapTask, ReduceCallable<T> reduceTask, ArrayList<String> files){
		this.mapTask    = mapTask;
		this.reduceTask = reduceTask;
		this.files      = files;
	}
	
	public MapCallable<U, T> getMapTask(){
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
