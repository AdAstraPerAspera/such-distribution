package mrf.example;

import java.util.ArrayList;

import mrf.tasks.AbstractMapCallable;

public class ImageMap extends AbstractMapCallable<ArrayList<Byte>, ArrayList<Byte>> {

	@Override
	public ArrayList<Byte> map(ArrayList<Byte> input) {
		ArrayList<Byte> res = new ArrayList<Byte>();
		for(int i = 0; i < input.size(); i++) {
			res.add((byte) (input.get(i) ^ 0xff));
		}
		return res;
	}
}
