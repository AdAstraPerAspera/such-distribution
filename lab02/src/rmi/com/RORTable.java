package rmi.com;

import java.util.Hashtable;
import rmi.reg.ReferenceObject;

/*
 * Implementation of a RemoteObjectReference Table.
 * 
 * @author - William Maynes (wmaynes)
 */

public class RORTable {
	
	private Hashtable<ReferenceObject, Object> H;
	private int objCount;
	
	public RORTable () {
		this.H = new Hashtable<ReferenceObject, Object>();
		this.objCount = 0;
	}
	
	public void addObj (String host, int port, String name, Object o) {
		if(o instanceof Remote440) {
			ReferenceObject ror = new ReferenceObject(host, port, objCount, name);
			this.objCount ++;
			H.put(ror, o);
		}
	}
	
	public Object findObj (ReferenceObject ror) {
		return H.get(ror);
	}
}
