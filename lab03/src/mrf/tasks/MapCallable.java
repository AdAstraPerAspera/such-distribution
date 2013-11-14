package mrf.tasks;

import mrf.dfs.MRFile;

public interface MapCallable<T> extends MRTask<T> {
	public void map(MRFile fileIn, MRFile fileOut);
}
