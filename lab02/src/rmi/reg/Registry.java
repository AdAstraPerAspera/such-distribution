package rmi.reg;

import java.util.Hashtable;

/*
 * Simple Registry for RMI
 * 
 * @author William Maynes - wmaynes
 */
public class Registry {

	private static String host;
	private static int port;

	private static Hashtable<String, ReferenceObject> H;
	
	/* TODO: Set up the registry's location and listening
	 */
	
	public Registry (String h, int p) {
		this.host = h;
		this.port = p;
		this.H = new Hashtable<String, ReferenceObject>();
	}

	public static ReferenceObject lookup (String str) {
		return H.get(str);
	}
	
	public static void unbind (String name) {
		H.remove(name);
	}
	
	public static void bind (String name, ReferenceObject ror) {
		if(!H.containsKey(name)) {
			H.put(name, ror);
		}
	}
	
	public static void rebind (String name, ReferenceObject ror) {
		unbind(name);
		bind(name, ror);
	}
}
