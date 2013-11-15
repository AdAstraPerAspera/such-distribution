package mrf.dfs;

import java.io.Serializable;

/**
 * Object used to send information to DFS Participants.
 * 
 * @author William Maynes - wmaynes
 *
 */

public class ConfigInfo implements Serializable {
	private String host;
	private String name;
	private int hostPort;
	private int regPort;
	
	public ConfigInfo(String name, String host, int port, int regPort) {
		this.host = host;
		this.name = name;
		this.hostPort = port;
		this.regPort = regPort;
	}
	
	public String getHost () {
		return this.host;
	}
	
	public String getName () {
		return this.name;
	}
	
	public int getPort () {
		return this.hostPort;
	}
	
	public int getRegPort(){
		return this.regPort;
	}
	/*
	public void setHost(String host) {
		this.host = host;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	*/

}
