package mrf.dfs;

import java.io.Serializable;
import java.util.ArrayList;

public class MRFile implements Serializable {
	ArrayList<Object> contents;
	String path;
	
	public MRFile (ArrayList<Object> A, String path) {
		this.contents = A;
		this.path = path;
	}
	
	public Object readEntry(int n) {
		return contents.get(n);
	}
}

