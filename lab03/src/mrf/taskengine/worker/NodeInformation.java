package mrf.taskengine.worker;

public class NodeInformation {
	private String hostname;
	private int    port;
	
	public NodeInformation(String hostname, int port){
		this.hostname = hostname;
		this.port     = port;
	}
	
	public String getHostname(){
		return hostname;
	}
	
	public int getPort(){
		return port;
	}
}
