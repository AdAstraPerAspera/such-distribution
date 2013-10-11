package rmi.com;

import java.net.URL;
import java.util.Hashtable;
import rmi.reg.ReferenceObject;

/**
 * Implementation of a RemoteObjectReference Table.
 * 
 * @author William Maynes - wmaynes
 */

public class RORTable {
	
	private Hashtable<ReferenceObject, Object> H;
	
	public RORTable () {
		this.H = new Hashtable<ReferenceObject, Object>();
	}
	
	public void addObj (String host, int port, String name, Object o, URL... urls) {
		if(o instanceof Remote440) {
			ReferenceObject ror = new ReferenceObject(host, port, name, o.getClass().getCanonicalName(), urls);
			H.put(ror, o);
		}
	}
	
	public Object findObj (ReferenceObject ror) {
		return H.get(ror);
	}
}
