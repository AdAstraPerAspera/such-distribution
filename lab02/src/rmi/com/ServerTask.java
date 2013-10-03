package rmi.com;

import java.io.*;
import java.net.*;

/**
 * A class that listens on a port and accepts RMI requests
 * 
 * @author Michael Wang - mhw1
 *
 */
public class ServerTask implements Runnable{
	
	private ServerSocket ssock;
	
	public ServerTask(ServerSocket ssock){
		this.ssock = ssock;
	}
	
	/**
	 * TODO: Add exception handling
	 * TODO: Call function on specified object
	 */
	public void run(){
		while(true){
			try{
				Socket sock = ssock.accept();
				InputStream         istream = sock.getInputStream();
				OutputStream        ostream = sock.getOutputStream();
				ObjectInputStream  oistream = new ObjectInputStream(istream);
				ObjectOutputStream oostream = new ObjectOutputStream(ostream);
				
				RMIInvocationMessage message = (RMIInvocationMessage) oistream.readObject();
				RMIResponseMessage response = null;
				
				//do stuff here
				
				//TODO: Call function on object using message
				
				//TODO: Return to function
				
				oostream.writeObject(response);
			} catch (Exception e){
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
		
}
