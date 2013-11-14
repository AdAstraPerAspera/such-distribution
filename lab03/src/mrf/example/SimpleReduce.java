package mrf.example;

import java.util.ArrayList;

import mrf.tasks.ReduceCallable;

public class SimpleReduce implements ReduceCallable<Integer> {

	@Override
	public Integer reduce(ArrayList<Integer> f) {
		int sum = 0;
		for(Integer i : f){
			sum += i;
		}
		return sum;
	}

}
