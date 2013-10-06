package rmi.reg;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

/**
 * Simple Registry for RMI
 * 
 * @author William Maynes - wmaynes
 * @author Michael Wang - mhw1
 */
public class Registry {

	private static String host;
	private static int port;

	private static Hashtable<String, ReferenceObject> H;
	
	private static ReferenceObject lookup(String str) {
		return H.get(str);
	}
	
	private static void unbind (String name) {
		H.remove(name);
	}
	
	private static void bind (String name, ReferenceObject ror) {
		if(!H.containsKey(name)) {
			H.put(name, ror);
		}
	}
	
	private static void rebind(String name, ReferenceObject ror) {
		unbind(name);
		bind(name, ror);
	}
	
	//TODO: Add socket stuff.
	public static void main(String[] args) throws Exception{
		host = (InetAddress.getLocalHost()).getHostName();
		port = Integer.parseInt(args[0]);
		H = new Hashtable<String, ReferenceObject>();
		
		ServerSocket ssock = new ServerSocket(port);
		
		while(true){
			Socket sock = ssock.accept();
			InputStream         istream = sock.getInputStream();
			OutputStream        ostream = sock.getOutputStream();
			ObjectInputStream  oistream = new ObjectInputStream(istream);
			ObjectOutputStream oostream = new ObjectOutputStream(ostream);
			
			RegistryRequest req = (RegistryRequest) oistream.readObject();
			
			switch(req.getRequestType()){
			case LOOKUP:
				oostream.writeObject(lookup(req.getName()));
				break;
				
			case UNBIND:
				unbind(req.getName());
				break;
			
			case BIND:
				bind(req.getName(), req.getRor());
				break;
			
			case REBIND:
				rebind(req.getName(), req.getRor());
				break;
				
			default:
				oostream.writeObject(new Integer(1));
			}
			
			oostream.close();
			oistream.close();
			ostream.close();
			istream.close();
			sock.close();
		}
	}
}
