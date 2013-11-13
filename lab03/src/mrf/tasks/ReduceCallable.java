package mrf.tasks;

import java.io.File;
import java.util.ArrayList;

public interface ReduceCallable<T> extends MRTask<T> {
	public T reduce(ArrayList<File> f);
}
