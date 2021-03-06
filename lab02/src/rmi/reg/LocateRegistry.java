package rmi.reg;

import java.lang.reflect.Method;
import java.net.*;
import java.io.*;

/**
 * Class used for locating and managing the registry
 * 
 * @author Michael Wang - mhw1
 */
public class LocateRegistry {
	private LocateRegistry(){}
	
	/**
	 * Checks if a registry exists at the specified port and host
	 * @param host - host to check at
	 * @param port - port to check at
	 * @return true if registry exists, false otherwise
	 */
	public static boolean hasRegistry(String host, int port){
		Socket  sock = null;
		Integer resp = null;
		try {
			sock = new Socket(host, port);
			RegistryRequest         req =
					new RegistryRequest(RegistryRequest.RequestType.PING, null, null);
			InputStream         istream = sock.getInputStream();
			OutputStream        ostream = sock.getOutputStream();
			ObjectOutputStream oostream = new ObjectOutputStream(ostream);
			oostream.writeObject(req);
			
			ObjectInputStream  oistream = new ObjectInputStream(istream);
			resp = (Integer) oistream.readObject();
			
			if(resp != null){
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			try {
				sock.close();
			} catch (Exception f) {
				return false;
			}
			return false;
		}
	}
	
	public static ReferenceObject registryLookup(String name, String host, int port){
		Socket          sock = null;
		ReferenceObject resp = null;
		
		try {
			sock = new Socket(host, port);
			RegistryRequest         req =
					new RegistryRequest(RegistryRequest.RequestType.LOOKUP, name, null);
			InputStream         istream = sock.getInputStream();
			OutputStream        ostream = sock.getOutputStream();
			
			ObjectOutputStream oostream = new ObjectOutputStream(ostream);
			oostream.writeObject(req);
			
			ObjectInputStream  oistream = new ObjectInputStream(istream);
			resp = (ReferenceObject) oistream.readObject();
			
		} catch (Exception e) {
			try {
				sock.close();
			} catch (IOException f) {
				return resp;
			}
		}
		
		return resp;
	}
	
	/**
	 * Creates a registry on the local host
	 * @param port - port for the registry to listen on
	 */
	public static void createRegistry(int port){
		try {
		    final Class<?> clazz = Class.forName("rmi.reg.Registry");
		    final Method method = clazz.getMethod("main", String[].class);

		    final Object[] args = new Object[1];
		    args[0] = new String[] { (new Integer(port)).toString()};
		    method.invoke(null, args);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
}
