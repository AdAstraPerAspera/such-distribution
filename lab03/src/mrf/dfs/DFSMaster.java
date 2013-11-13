package mrf.dfs;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashSet;

public interface DFSMaster extends Remote {
	
	public HashSet<String> lookupFile(String name) throws RemoteException;
	
	public void distributeFile(MRFile f) throws RemoteException;

}
