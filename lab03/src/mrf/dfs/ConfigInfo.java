package mrf.dfs;

import java.io.Serializable;

public class ConfigInfo implements Serializable {
	String host;
	String name;
	
	public ConfigInfo(String host, String name) {
		this.host = host;
		this.name = name;
	}
	
	public String getHost () {
		return this.host;
	}
	
	public String getName () {
		return this.name;
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
