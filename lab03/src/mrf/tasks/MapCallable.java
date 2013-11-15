package mrf.tasks;

import java.io.Serializable;

import mrf.dfs.MRFile;

/**
 * For use with MRTask
 * 
 * @author Michael Wang - mhw1
 *
 * @param <U>
 * @param <T>
 */
public interface MapCallable<U, T> extends Serializable {
	public T map(U input);
	
	public void map(MRFile fileIn, String fileOut);
}
