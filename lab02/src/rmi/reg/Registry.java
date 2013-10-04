package rmi.reg;

import java.util.Hashtable;

public class Registry {
	
	public class Pair<X, Y> {
		public X x;
		public Y y;
		public Pair(X x, Y y){
			this.x = x;
			this.y = y;
		}
	}
	
	private String host;
	private int port;
	
	private Hashtable<String, RefObject> H = new Hashtable<String, RefObject>();
	
	/* TODO: Set up the registry's location and listening
	 *  
	 * TODO: Constructor
	 * 
	 */

	public RefObject lookup (String str) {
		RefObject ref = H.get(str);
		return ref;
	}
	
	// TODO: Implement Bind
	public void bind (String str) {
		
	}
	
	// TODO: Implement Migrate
	public void migrate (String str){
		
	}
	
}
