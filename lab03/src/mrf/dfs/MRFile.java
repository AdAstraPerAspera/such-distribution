package mrf.dfs;

import java.io.Serializable;
import java.util.ArrayList;

public class MRFile implements Serializable {
	private ArrayList<Object> contents;
	private String name;
	
	public MRFile (ArrayList<Object> A, String name) {
		this.contents = A;
		this.name = name;
	}
	
	public Object readEntry (int n) throws Exception {
		if (n >= contents.size()) {
			throw new Exception("Attempting to read past End Of File");
		} else {
			return contents.get(n);
		}
	}
	
	public int size () {
		return contents.size();
	}
	
	public String getName () {
		return this.name;
	}
	
	public ArrayList<Object> getContents () {
		return this.contents;
	}
}

