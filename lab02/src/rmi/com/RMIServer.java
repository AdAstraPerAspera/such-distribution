package rmi.com;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.*;

public class RMIServer{
	
	private static String   host;
	private static int      port;
	private static RORTable t = null;
	private static boolean  running = false;
	
	public static void bind(String name, Remote440 o) throws Exception{
		if(!running) throw new Exception("RMI Server is not running at the moment");
		try {
			t.addObj((InetAddress.getLocalHost()).getHostName(), port, name, o);
		} catch (Exception e) {
			throw new Exception("Failed to get local hostname!");
		}
	}
	
	/**
	 * TODO: Add exception handling
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		host = (InetAddress.getLocalHost()).getHostName();
		port                = Integer.parseInt(args[0]);
		String registryHost = args[1];
		int    registryPort = Integer.parseInt(args[2]);
		String serviceName  = args[3];

		if(t == null) t = new RORTable();
		
		//TODO: register in registry
		
		ServerSocket ssock = new ServerSocket(port);
		
		while(true){
			//Read in message
			Socket sock = ssock.accept();
			InputStream         istream = sock.getInputStream();
			OutputStream        ostream = sock.getOutputStream();
			ObjectInputStream  oistream = new ObjectInputStream(istream);
			ObjectOutputStream oostream = new ObjectOutputStream(ostream);
			
			RMIInvocationMessage message = (RMIInvocationMessage) oistream.readObject();
			RMIResponseMessage response = null;
			
			//pull local copy of object from ror table
			Object obj = t.findObj(message.getObj());
			Class clazz = Class.forName(message.getTypeName());
			
			//get function from local object using reflections
			Serializable[] arguments = message.getParams();
			Class[] parameterTypes = new Class[arguments.length];
			
			for (int i = 0; i < arguments.length; i++){
				parameterTypes[i] = arguments[i].getClass();
			}
			
			try {
				Method methods = clazz.getDeclaredMethod(message.getFunc(), parameterTypes);
			} catch (NoSuchMethodException e){
				
			}
			clazz.cast(obj);
			//do stuff here
			//TODO: Return to function
			
			oostream.writeObject(response);
			
			oostream.close();
			oistream.close();
			ostream.close();
			istream.close();
			sock.close();
		}
	}
}
