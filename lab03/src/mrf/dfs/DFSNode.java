package mrf.dfs;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;



public interface DFSNode extends Remote {
	
	public void writeFile(File f) throws RemoteException;
	
	public File getFile(String name) throws RemoteException;

}
