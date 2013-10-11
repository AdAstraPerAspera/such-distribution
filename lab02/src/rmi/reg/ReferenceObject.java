package rmi.reg;

import java.io.Serializable;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

import rmi.com.Stub;

/**
 * Basic implementation of a Remote Object Reference. 
 * 
 * @author William Maynes - wmaynes
 */

public class ReferenceObject implements Serializable {
	private String host;
	private int port;
	private String name;
	private String type;
	private URL[] urls;
	
	public ReferenceObject (String h, int p, String n, String t, URL... urls){
		this.host = h;
		this.port = p;
		this.name = n;
		this.type = t;
		this.urls = urls;
	}
	
	public String getHost () {
		return this.host;
	}
	
	public int getPort () {
		return this.port;
	}
	
	public String getName () {
		return this.name;
	}
	
	public String getType(){
		return this.type;
	}
	
	/**
	 * Generates a stub for the object that this ReferenceObject represents.
	 * @return Stub corresponding to this remote object
	 */
	public Stub localize() throws Exception {
		URLClassLoader load = new URLClassLoader(urls);
		Class clazz = Class.forName(type + "_Stub");
		if(clazz == null) clazz = load.loadClass(type + "_Stub");
		Stub s = (Stub) clazz.newInstance();
		s.setROR(this);
		return s;
	}
	
	/**
	 * For debugging purposes
	 */
	@Override
	public String toString(){
		return String.format("Host: %s\nPort: %d\nName: %s\nType: %s", host, port, name, type);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + port;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + Arrays.hashCode(urls);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReferenceObject other = (ReferenceObject) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (port != other.port)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (!Arrays.equals(urls, other.urls))
			return false;
		return true;
	}
	
	
}
