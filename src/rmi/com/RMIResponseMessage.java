package rmi.com;

import java.io.Serializable;
import java.util.ArrayList;

public class RMIResponseMessage implements Serializable {
	private Serializable[] params;
	private Serializable   retVal;
	
	/**
	 * 
	 * @param objID the unique identifier for object on which the method is to be invoked
	 * @param funcID the name of the function to invoke
	 * @param args necessary arguments to invoke the function, if any
	 */
	public RMIResponseMessage(Serializable retVal, Serializable... args){
		this.retVal = retVal;
		ArrayList<Serializable> paramList = new ArrayList<Serializable>();
		for (Serializable arg : args){
			paramList.add(arg);
		}
		
		this.params = new Serializable[paramList.size()];
		
		for (int i = 0; i < paramList.size(); i++){
			this.params[i] = paramList.get(i);
		}
	}
	
	public Serializable[] getParams(){
		return params;
	}
	
	public Serializable getRetVal(){
		return retVal;
	}
}
