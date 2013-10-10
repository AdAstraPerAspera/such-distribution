package rmi.com;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A RMI message class, used to invoke remote methods.
 * 
 * @author Michael Wang - mhw1
 */
public class RMIInvocationMessage implements Serializable {
	private String objID;
	private String funcID;
	private Serializable[] params;
	private String[] urls;
	
	/**
	 * 
	 * @param objID the unique identifier for object on which the method is to be invoked
	 * @param funcID the name of the function to invoke
	 * @param args necessary arguments to invoke the function, if any
	 */
	public RMIInvocationMessage(String objID, String funcID, String[] urls, Serializable... args){
		this.objID  = objID;
		this.funcID = funcID;
		this.urls   = urls;
		ArrayList<Serializable> paramList = new ArrayList<Serializable>();
		for (Serializable arg : args){
			paramList.add(arg);
		}
		
		this.params = new Serializable[paramList.size()];
		
		for (int i = 0; i < paramList.size(); i++){
			this.params[i] = paramList.get(i);
		}
	}
	
	public String getObj(){
		return objID;
	}
	
	public String getFunc(){
		return funcID;
	}
	
	public Serializable[] getParams(){
		return params;
	}
}
