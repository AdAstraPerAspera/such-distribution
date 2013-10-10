package rmi.reg;

import java.io.Serializable;

/**
 * Class to encapsulate requests to the registry 
 *  
 * @author Michael Wang - mhw1
 */
public class RegistryRequest implements Serializable {
	/**
	 * Enum that specifies the request types 
	 */
	public enum RequestType{
		LOOKUP, UNBIND, BIND, REBIND, PING;
	}
	
	private RequestType     type;
	private String          name;
	private ReferenceObject ror;
	
	public RegistryRequest(RequestType type, String name, ReferenceObject ror){
		this.type = type;
		this.name = name;
		this.ror  = ror;
	}
	
	public RequestType getRequestType(){
		return type;
	}
	
	public String getName(){
		return name;
	}
	
	public ReferenceObject getRor(){
		return ror;
	}
}
