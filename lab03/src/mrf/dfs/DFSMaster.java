package mrf.dfs;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Remote Interface for the Coordinator
 * 
 * @author William Maynes - wmaynes
 *
 */

public interface DFSMaster extends Remote {
	
	public Set<String> listFiles() throws RemoteException;
	
	public HashSet<String> lookupFile(String name) throws RemoteException;
	
	public void distributeFile(MRFile f) throws RemoteException;
}
