package rmi.reg;

import java.io.Serializable;

public class RefObject implements Serializable {
	private String host;
	private int port;
	
	public RefObject (String h, int p){
		this.host = h;
		this.port= p;
	}	
}
