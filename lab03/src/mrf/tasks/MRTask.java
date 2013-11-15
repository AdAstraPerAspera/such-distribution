package mrf.tasks;

import java.io.Serializable;
import java.util.List;

/**
 * Interface for a map reduce task
 * 
 * @author Michael Wang - mhw1
 *
 * @param <U> - type to map from
 * @param <T> - type to map/reduce to
 */
public interface MRTask<U, T> extends Serializable {
	/**
	 * Gets the map task
	 * @return
	 */
	public MapCallable<U, T> getMapTask();
	
	/**
	 * Gets the reduce task
	 * @return
	 */
	public ReduceCallable<T> getReduceTask();
	
	/**
	 * Gets the list of files needed
	 * @return
	 */
	public List<String> getFiles();
	
	/**
	 * Gets the task's name
	 * @return
	 */
	public String getName();
}
