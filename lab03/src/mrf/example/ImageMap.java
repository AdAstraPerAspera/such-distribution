package mrf.example;

import java.util.ArrayList;

import mrf.tasks.AbstractMapCallable;

public class ImageMap extends AbstractMapCallable<ArrayList<Integer>, ArrayList<Integer>> {

	@Override
	public ArrayList<Integer> map(ArrayList<Integer> input) {
		ArrayList<Integer> res = new ArrayList<Integer>();
		for(int i = 0; i < input.size(); i++) {
			res.add((int) (input.get(i) ^ 0x00ffffff));
		}
		return res;
	}
}
