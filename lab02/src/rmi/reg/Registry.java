package rmi.reg;

import java.util.Hashtable;

public class Registry {

	private String host;
	private int port;
	
	private Hashtable<String, ReferenceObject> H = new Hashtable<String, ReferenceObject>();
	
	/* TODO: Set up the registry's location and listening
	 *  
	 * TODO: Constructor
	 * 
	 */
	
	public Registry (String h, int p) {
		
	}

	public ReferenceObject lookup (String str) {
		ReferenceObject ref = H.get(str);
		return ref;
	}
	
	// TODO: Implement Bind
	public void bind (String str) {
		
	}
	
	// TODO: Implement Migrate
	public void migrate (String str){
		
	}
	
	// TODO: Implement End Migrate
	public void endMigrate (String str){
		
	}
	
}
