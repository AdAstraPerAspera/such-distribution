package mrf.tasks;

import java.io.Serializable;

public interface MRTask<U, T> extends Serializable {
	public MapCallable<U, T> getMapTask();
	
	public ReduceCallable<T> getReduceTask();
}
