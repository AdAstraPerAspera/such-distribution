package mrf.taskengine.worker;

import java.rmi.RemoteException;

import mrf.dfs.DFSNode;
import mrf.dfs.MRFile;
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
			MRFile fileIn  = node.getFile(pathIn);
			MRFile fileOut = node.getFile(pathOut);
			m.map(fileIn, fileOut);
		} catch (RemoteException e) {
			// execution should never reach here
			e.printStackTrace();
		}
	}
}
