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

	public static Object invoke (String fnId, String name, ReferenceObject ror, Serializable... args) 
			throws RemoteException{
		try {
			// Set up the network connections.
			Socket S = new Socket(ror.getHost(), ror.getPort());
			ObjectInputStream ois = new ObjectInputStream(S.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(S.getOutputStream());
			
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
			
			RMIInvocationMessage invMsg = new RMIInvocationMessage(name, ror, fnId, A);
			
			oos.writeObject(invMsg);
			RMIResponseMessage respMsg = (RMIResponseMessage) ois.readObject();
			
			if(respMsg.getExcept() != null) throw new RemoteException();
			
			// Reset any possible side effects that happened when we called our function.
			Serializable[] params = respMsg.getParams();
			for(int i = 0; i < params.length; i++){
				if(!(params[i] instanceof ReferenceObject)) {
					args[i] = params[i];
				}
			}
			
			return respMsg.getRetVal();
		} catch (Exception e) {
			throw new RemoteException();
		}
	}
	
}
