package rmi.com;

/*
 * Remote exception for our 15-440 RMI project.
 * 
 * @author William Maynes - wmaynes
 */

public class RemoteException extends Exception {

		public RemoteException () {}
		
		public RemoteException (String message) {
			super(message);
		}
}
