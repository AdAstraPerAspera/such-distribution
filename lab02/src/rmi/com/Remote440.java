package rmi.com;

/**
 * An interface for remote objects.  Any object intended for use with this RMI
 * facility must implement this interface. 
 * 
 * @author Michael Wang - mhw1
 */
public interface Remote440 {
	
	/**
	 * Processes an RMIMessage object and invokes the function referenced
	 * therein.  Throws an exception if the function ID is invalid.
	 * @param message - the message invoking the method
	 * @return RMIMessage containing the return value and arguments to the
	 * function
	 */
	public RMIResponseMessage remoteCall(RMIInvocationMessage message) throws Exception;
	
}
