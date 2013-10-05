package rmi.com;

import java.net.*;

public class ServerHost {
	
	private static ServerSocket[] sockets;
	private static int[]          ports;
	
	/**
	 * TODO: Add exception handling
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		ports = new int[PortInfo.NUM_SOCK];
		
		for(int i = 0; i < ports.length; i++){
			sockets[i] = new ServerSocket(ports[i]);
		}
		
		for(ServerSocket s : sockets){
			ServerTask task = new ServerTask(s);
			Thread t = new Thread(task);
			t.run();
		}
	}
}
