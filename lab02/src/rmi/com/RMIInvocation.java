package rmi.com;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import rmi.reg.ReferenceObject;

/*
 * Client side communication protocal to shield them from sockets and networking.
 * 
 * @author William Maynes - wmaynes
 */

public class RMIInvocation {

	
	public RMIInvocation() {

	}
	
	public static Object invoke (String fnId, String name, ReferenceObject ror, Serializable... args) 
		throws RemoteException{
		try {
			Socket S = new Socket(ror.getHost(), ror.getPort());
			ObjectInputStream ois = new ObjectInputStream(S.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(S.getOutputStream());
			RMIInvocationMessage invMsg = new RMIInvocationMessage(name, ror, fnId, args);
			oos.writeObject(invMsg);
			RMIResponseMessage respMsg = (RMIResponseMessage) ois.readObject();
			
		} catch (Exception e) {
			throw new RemoteException();
		}
		return ror;
	}
	
}
