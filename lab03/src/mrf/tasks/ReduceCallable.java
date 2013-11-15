package mrf.tasks;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * For use with MRTask
 * 
 * @author Michael Wang - mhw1
 *
 * @param <T>
 */
public interface ReduceCallable<T> extends Serializable {
	/**
	 * Reduces a list of objects 
	 * @param f - list to reduce
	 * @return
	 */
	public T reduce(ArrayList<T> f);
}
