package mrf.example;

import java.util.ArrayList;

import mrf.tasks.ReduceCallable;

public class ImageReduce implements ReduceCallable<ArrayList<Byte>> {

	@Override
	public ArrayList<Byte> reduce(ArrayList<ArrayList<Byte>> f) {
		ArrayList<Byte> res = new ArrayList<Byte>();
		for(int i = 0; i < f.size(); i++){
			res.addAll(f.get(i));
		}
		return res;
	}
}
