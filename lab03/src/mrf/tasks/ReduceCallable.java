package mrf.tasks;

import java.io.Serializable;
import java.util.ArrayList;

public interface ReduceCallable<T> extends Serializable {
	public T reduce(ArrayList<T> f);
}
