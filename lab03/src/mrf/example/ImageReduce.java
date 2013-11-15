package mrf.example;

import java.util.ArrayList;

import mrf.tasks.ReduceCallable;

public class ImageReduce implements ReduceCallable<ArrayList<Integer>> {

	@Override
	public ArrayList<Integer> reduce(ArrayList<ArrayList<Integer>> f) {
		ArrayList<Integer> res = new ArrayList<Integer>();
		for(int i = 0; i < f.size(); i++){
			res.addAll(f.get(i));
		}
		return res;
	}
}
