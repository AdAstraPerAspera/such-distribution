package mrf.taskengine.worker;

import java.util.ArrayList;

import mrf.dfs.DFSNode;
import mrf.tasks.ReduceCallable;

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
