package mrf.taskengine.master;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TaskMaster extends Remote {
	public void doneMapping(String nodeName) throws RemoteException;
	
	public <T> void doneReducing(String nodeName, T result) throws RemoteException;
}
