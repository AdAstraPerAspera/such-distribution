package rmi.com;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import rmi.reg.ReferenceObject;
import rmi.test.FieldClass;

/**
 * Client side communication protocol to shield them from sockets and networking.
 * 
 * @author William Maynes - wmaynes
 */

public class RMIInvocation {

	public static Object invoke (String fnId, String type, ReferenceObject ror, Serializable... args) 
			throws RemoteException{
		try {
			// Set up the network connections.
			Socket S = new Socket(ror.getHost(), ror.getPort());
			
			// If the call is being made with any stubs, replace the stub with a ROR.
			Serializable[] A = new Serializable[args.length];
			for(int i = 0; i < args.length; i++){
				if(args[i] instanceof Stub) {
					A[i] = ((Stub) args[i]).getRef();
				}
				else {
					A[i] = args[i];
				}
			}
			
			RMIInvocationMessage invMsg = new RMIInvocationMessage(type, ror, fnId, A);
			
			ObjectOutputStream oos = new ObjectOutputStream(S.getOutputStream());
			oos.writeObject(invMsg);
			
			ObjectInputStream ois = new ObjectInputStream(S.getInputStream());
			RMIResponseMessage respMsg = (RMIResponseMessage) ois.readObject();
			
			if(respMsg.getExcept() != null) throw new RemoteException(respMsg.getExcept().getMessage());
			
			return respMsg.getRetVal();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
	}
	
}
