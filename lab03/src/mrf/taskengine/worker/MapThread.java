package mrf.taskengine.worker;

import java.io.File;
import java.rmi.RemoteException;

import mrf.dfs.DFSNode;
import mrf.tasks.MapCallable;

public class MapThread<T> {
	private DFSNode        node;
	private MapCallable<T> m;
	private String         pathIn;
	private String         pathOut;
	
	public MapThread(DFSNode node, MapCallable<T> m, String pathIn, String pathOut){
		this.node    = node;
		this.m       = m;
		this.pathIn  = pathIn;
		this.pathOut = pathOut;
	}
	
	public void run() {
		try {
			File fileIn = node.getFile(pathIn);
			File fileOut = node.getFile(pathOut);
			m.map(fileIn, fileOut);
		} catch (RemoteException e) {
			// execution should never reach here
			e.printStackTrace();
		}
	}
}