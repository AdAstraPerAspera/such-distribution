package mrf.dfs;

import java.io.Serializable;

public class ConfigInfo implements Serializable {
	String host;
	String name;
	int hostPort;
	
	public ConfigInfo(String name, String host, int port) {
		this.host = host;
		this.name = name;
		this.hostPort = port;
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
	/*
	public void setHost(String host) {
		this.host = host;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	*/

}
