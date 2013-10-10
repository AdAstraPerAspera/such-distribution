package rmi.com;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.*;

public class RMIServer{
	
	private static String   host;
	private static int      port;
	private static RORTable t = null;
	
	/**
	 * TODO: Add exception handling
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		String InitialClassName = args[0];
		String registryHost = args[1];
		int registryPort = Integer.parseInt(args[2]);	
		String serviceName = args[3];

		host = (InetAddress.getLocalHost()).getHostName();
		port = 15440;

		Class initialclass = Class.forName(InitialClassName);
		Object o = initialclass.newInstance();
		
		if(t == null) t = new RORTable();
		t.addObj(host, port, serviceName, o);
		
		//TODO: register in registry
		
		ServerSocket ssock = new ServerSocket(port);
		
		while(true){
			Socket sock = ssock.accept();
			InputStream         istream = sock.getInputStream();
			OutputStream        ostream = sock.getOutputStream();
			ObjectInputStream  oistream = new ObjectInputStream(istream);
			ObjectOutputStream oostream = new ObjectOutputStream(ostream);
			
			RMIInvocationMessage message = (RMIInvocationMessage) oistream.readObject();
			RMIResponseMessage response = null;
			

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
