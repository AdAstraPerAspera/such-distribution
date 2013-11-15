package mrf.example;

import mrf.tasks.AbstractMapCallable;

/**
 * Simple map example with integers
 * 
 * @author Michael Wang - mhw1
 * @author William Maynes - wmaynes
 *
 */

public class SimpleMap extends AbstractMapCallable<Integer, Integer> {

	@Override
	public Integer map(Integer input) {
		return input + 1;
	}

}
