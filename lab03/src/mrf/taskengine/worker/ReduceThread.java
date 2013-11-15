package mrf.taskengine.worker;

import java.util.ArrayList;

import mrf.tasks.ReduceCallable;

/**
 * Callable to run a reduce task
 * 
 * @author Michael Wang - mhw1
 *
 * @param <T> - type of object to reduce
 */
public class ReduceThread<T> {
	private ReduceCallable<T> m;
	private ArrayList<T>      data;
	
	public ReduceThread(ReduceCallable<T> m, ArrayList<T> data){
		this.m    = m;
		this.data = data;
	}
	
	public T run() {
		return m.reduce(data);
	}
}
