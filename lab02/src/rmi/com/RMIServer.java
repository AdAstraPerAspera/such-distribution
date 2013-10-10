package rmi.com;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.*;

import rmi.reg.LocateRegistry;
import rmi.reg.ReferenceObject;
import rmi.reg.RegistryRequest;

/**
 * The RMI Server that receives invocation requests from clients.
 * Requires four arguments when starting on the command line:
 * The host name used for external locations.
 * The port that this server listens to.
 * The host name used to connect to the registry (to bind remote objects).
 * The port that the registry listens to. 
 * 
 * Single threaded, single socket.
 * 
 * @author Michael Wang - mhw1
 */
public class RMIServer{
	//Static variables ensures that only one instance of the server can run on any host
	private static String   host;
	private static int      port;
	private static String   registryHost;
	private static int      registryPort;
	private static RORTable t = null;
	private static boolean  running = false;
	
	/**
	 * Static method.  Call to bind a remote object to the RORTable.
	 * @param name - name to bind the remote object to
	 * @param o - the object itself
	 * @param urls - URLs where the .class files for the stub for the object can be found
	 * @throws Exception - If the RMI server is not running or if there is a failure to contact the registry
	 */
	public static void bind(String name, Remote440 o, URL... urls) throws Exception{
		if(!running) throw new Exception("RMI Server is not running at the moment");
		try {
			//Add object to local ror table
			t.addObj(host, port, name, o, urls);
			
			if(!LocateRegistry.hasRegistry(registryHost, registryPort)) throw new Exception("Registry does not exist!");
			
			//bind object to registry
			ReferenceObject ror = new ReferenceObject(host, port, name, urls);
			RegistryRequest req = new RegistryRequest(RegistryRequest.RequestType.BIND, name, ror);
			
			Socket             sock     = new Socket(registryHost, registryPort);
			OutputStream       ostream  = sock.getOutputStream();
			ObjectOutputStream oostream = new ObjectOutputStream(ostream);
			
			oostream.writeObject(req);
			
			oostream.close();
			ostream.close();
			sock.close();
		} catch (Exception e) {
			throw new Exception("Failure to bind to registry");
		}
	}
	
	/**
	 * The main method for the RMI Server.  
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		host         = args[0];
		port         = Integer.parseInt(args[1]);
		registryHost = args[2];
		registryPort = Integer.parseInt(args[3]);

		if(t == null) t = new RORTable();
		
		running = true;
		
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
			
			boolean      isVoid    = false;
			Serializable ret       = null;
			Object[]     newParams = message.getParams();
			
			//replace RORs with stubs
			for(int i = 0; i < newParams.length; i++){
				if(newParams[i] instanceof ReferenceObject){
					newParams[i] = newParams[i].localize();
				}
			}
			
			//invoke method
			try {
				Method method = clazz.getDeclaredMethod(message.getFunc(), parameterTypes);
				method.invoke(clazz.cast(obj), newParams);
				
				if(method.getReturnType().equals(Void.TYPE)){
					isVoid = true;
					method.invoke(clazz.cast(obj), newParams);
				} else {
					ret = (Serializable) method.invoke(clazz.cast(obj), newParams); 
				}
			} catch (Exception e){
				//if invocation message throws an exception, send that exception back to the client
				response = new RMIResponseMessage(e);
				oostream.writeObject(response);
				
				oostream.close();
				oistream.close();
				ostream.close();
				istream.close();
				sock.close();
				continue;
			}
			
			//generate response message
			if(isVoid){
				response = new RMIResponseMessage(null, isVoid, message.getParams());
			} else {
				response = new RMIResponseMessage(ret, isVoid, message.getParams());
			}
			
			//send response back to client
			oostream.writeObject(response);
			
			oostream.close();
			oistream.close();
			ostream.close();
			istream.close();
			sock.close();
		}
	}
}
