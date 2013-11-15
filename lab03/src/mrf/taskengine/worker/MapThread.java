package mrf.taskengine.worker;

import java.rmi.RemoteException;

import mrf.dfs.DFSNode;
import mrf.dfs.MRFile;
import mrf.tasks.MapCallable;

/**
 * Callable that runs a map task.
 * 
 * @author Michael Wang - mhw1
 *
 * @param <U> - class to map from
 * @param <T> - class to map to
 */
public class MapThread<U, T> {
	private DFSNode           node;
	private MapCallable<U, T> m;
	private String            pathIn;
	private String            pathOut;
	
	public MapThread(DFSNode node, MapCallable<U, T> m, String pathIn, String pathOut){
		this.node    = node;
		this.m       = m;
		this.pathIn  = pathIn;
		this.pathOut = pathOut;
	}
	
	public void run() {
		try {
			MRFile fileIn  = node.getFile(pathIn);
			m.map(fileIn, pathOut);
		} catch (RemoteException e) {
			// execution should never reach here
			e.printStackTrace();
		}
	}
}
