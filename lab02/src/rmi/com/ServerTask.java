package rmi.com;

public class ServerTask implements Runnable {
	
	private String host;
	private int    port;
	private String rHost;
	private int    rPort;
	
	public ServerTask(String host, int port, String rHost, int rPort){
		this.host  = host;
		this.port  = port;
		this.rHost = rHost;
		this.rPort = rPort;
	}
	
	public void run(){
		RMIServer.startServerMain(host, port, rHost, rPort);
	}

}
