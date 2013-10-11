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
 * 
 * Can be started either in a thread of its own or by the command line.
 * 
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
public class RMIServer {
	private static final int MAX_CONNECTIONS = 5;
	
	//Static variables ensures that only one instance of the server can run on any host
	private static String   host;
	private static int      port;
	private static String   registryHost;
	private static int      registryPort;
	private static RORTable t = null;
	private static boolean  running = false;
	
	public RMIServer(String host, int port, String registryHost, int registryPort){
		RMIServer.host = host;
		RMIServer.port = port;
		RMIServer.registryHost = registryHost;
		RMIServer.registryPort = registryPort;
	}
	
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
			ReferenceObject ror = new ReferenceObject(host, port, name, o.getClass().getCanonicalName(), urls);
			RegistryRequest req = new RegistryRequest(RegistryRequest.RequestType.BIND, name, ror);
			
			Socket       sock    = new Socket(registryHost, registryPort);
			OutputStream ostream = sock.getOutputStream();
			InputStream  istream = sock.getInputStream(); 
			
			ObjectOutputStream oostream = new ObjectOutputStream(ostream);
			oostream.writeObject(req);
			
			ObjectInputStream  oistream = new ObjectInputStream(istream);
			oistream.readObject();
			
			oostream.close();
			oistream.close();
			ostream.close();
			istream.close();
			sock.close();
		} catch (Exception e) {
			throw new Exception("Failure to bind to registry");
		}
	}
	
	/**
	 * Static method that allows the server to be started using the main method
	 * @param host - host IP of the server
	 * @param port - port to listen on
	 * @param registryHost - host IP of the registry
	 * @param registryPort - port that the registry listens on
	 */
	public static void startServerMain(String host, int port, String registryHost, int registryPort){
		try {
		    final Class<?> clazz = Class.forName("rmi.com.RMIServer");
		    final Method method = clazz.getMethod("main", String[].class);

		    final Object[] args = new Object[1];
		    args[0] = new String[] { host,         (new Integer(port)).toString(),
		    						 registryHost, (new Integer(registryPort)).toString() };
		    method.invoke(null, args);
		} catch (Exception e) {
		    e.printStackTrace();
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
		
		for(int i = 0; i < MAX_CONNECTIONS; i++){
			RMIServerTask task = new RMIServerTask(ssock, t);
			Thread thr = new Thread(task);
			thr.start();
		}
	}
}
