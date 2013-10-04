package rmi.reg;

import java.io.Serializable;

public class RefObject implements Serializable {
	private String host;
	private int port;
	private boolean migrating;
	
	public RefObject (String h, int p){
		this.host = h;
		this.port = p;
		this.migrating = false;
	}
	
	// TODO : Better errors for getPort and getHost if called while 
	// they are being migrated.
	public int getPort () {
		synchronized(this) {
			if (migrating == false) {
				return port;
			}
			else {
				return 0;
			}
		}
	}
	
	public String getHost () {
		synchronized(this) {
			if (migrating == false) {
				return host;
			}
			else {
				return "";
			}
		}
	}
	
	public void startMigrate () {
		synchronized(this) {
			migrating = true;
		}
	}
	
	public void endMigrate () {
		synchronized(this) {
			migrating = false;
		}
	}
}
