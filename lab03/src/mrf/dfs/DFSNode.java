package mrf.dfs;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote Interface for the Participant
 * 
 * @author William Maynes - wmaynes
 *
 */

public interface DFSNode extends Remote, Serializable {
	
	public void writeFile(MRFile f) throws RemoteException;
	
	public MRFile getFile(String name) throws RemoteException;

}
