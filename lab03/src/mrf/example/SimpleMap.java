package mrf.example;

import mrf.tasks.AbstractMapCallable;

public class SimpleMap extends AbstractMapCallable<Integer, Integer> {

	@Override
	public Integer map(Integer input) {
		return input + 1;
	}

}
