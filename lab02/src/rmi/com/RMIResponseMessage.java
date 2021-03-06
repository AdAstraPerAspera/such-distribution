package rmi.com;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class to encapsulate responses made in response to RMI invocation requests  
 * 
 * @author Michael Wang - mhw1
 */
public class RMIResponseMessage implements Serializable {
	private Serializable   retVal;
	private Exception      except;
	private boolean        isVoid;
	
	/**
	 * 
	 * @param retVal - the return value of the function, if any
	 * @param isVoid - true if the return type of the function invoked was void, false otherwise
	 * @param args   - arguments passed into the function as parameters during invocation
	 */
	public RMIResponseMessage(Serializable retVal, boolean isVoid){
		this.retVal = retVal;
		this.isVoid = isVoid;
		this.except = null;
	}
	
	/**
	 * 
	 * @param e - the exception that was thrown during function invocation
	 */
	public RMIResponseMessage(Exception e){
		this.except = e;
		this.isVoid = false;
		this.retVal = null;
	}
	
	public Exception getExcept(){
		return except;
	}
	
	public Serializable getRetVal(){
		return retVal;
	}
}
