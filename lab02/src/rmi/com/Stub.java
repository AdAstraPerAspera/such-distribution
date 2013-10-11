package rmi.com;

import rmi.reg.ReferenceObject;

/**
 * Stub interface to identify if we are being passed a stub 
 * 
 * @author William Maynes - wmaynes
 */

public interface Stub {
	
	public ReferenceObject getRef();

	/**
	 * Associates this stub with a specific reference object.
	 * @param ror
	 */
	public void setROR(ReferenceObject ror);

}
