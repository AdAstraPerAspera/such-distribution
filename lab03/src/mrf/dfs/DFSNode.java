package mrf.dfs;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;



public interface DFSNode extends Remote {
	
	public void writeFile(MRFile f) throws RemoteException;
	
	public MRFile getFile(String name) throws RemoteException;

}
