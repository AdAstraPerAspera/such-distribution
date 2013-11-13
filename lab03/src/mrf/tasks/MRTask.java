package mrf.tasks;

import java.io.Serializable;

public interface MRTask<T> extends Serializable {
	public MapCallable<T> getMapTask();
	
	public ReduceCallable<T> getReduceTask();
}
