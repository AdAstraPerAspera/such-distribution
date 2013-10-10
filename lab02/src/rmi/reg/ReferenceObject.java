package rmi.reg;

import java.io.Serializable;

/*
 * Basic implementation of a Remote Object Reference. 
 * 
 * @author William Maynes - wmaynes
 */

public class ReferenceObject implements Serializable {
	private String host;
	private int port;
	private String name;
	private int key;
	
	public ReferenceObject (String h, int p, int k, String n){
		this.host = h;
		this.port = p;
		this.name = n;
		this.key = k;
	}
	
	public Object localize() {
		String stubName = name + "_stub";
		Class c;
		try {
			c = Class.forName(stubName);
			Object o = c.newInstance();
			return o;
		} catch (Exception e) {
			System.out.print(e);
			return null;
		}
	}
	
	public String getHost () {
		return this.host;
	}
	
	public int getPort () {
		return this.port;
	}
	
	public int getKey () {
		return this.key;
	}
	
	public String getName () {
		return this.name;
	}
}
