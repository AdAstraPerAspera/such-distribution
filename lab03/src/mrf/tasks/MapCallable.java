package mrf.tasks;

import java.io.File;

public interface MapCallable<T> extends MRTask<T> {
	public void map(File in, File out);
}
