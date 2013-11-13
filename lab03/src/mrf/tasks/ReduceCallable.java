package mrf.tasks;

import java.util.ArrayList;

public interface ReduceCallable<T> extends MRTask<T> {
	public T reduce(ArrayList<T> f);
}
