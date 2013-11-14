package mrf.tasks;

import java.io.Serializable;

import mrf.dfs.MRFile;

public interface MapCallable<U, T> extends Serializable {
	public T map(U input);
	
	public void map(MRFile fileIn, String fileOut);
}
