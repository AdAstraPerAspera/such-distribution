package rmi.com;

/**
 * Remote exception for our 15-440 RMI project.
 * 
 * @author William Maynes - wmaynes
 */

public class RemoteException extends Exception {

	// Constructor without arguments
	public RemoteException () {}
		
	// Constructor if you have a message
	public RemoteException (String message) {
		super(message);
	}
}
