package mrf.admin.manager;

import java.util.HashMap;
import java.util.HashSet;

public class ConfigData {
	private HashMap<String, HashSet<String>> part2file;
	private HashMap<String, String> part2loc;
	private int repfactor = 0;
	private int chunksize = 0;
	
	private String initData;
	
	private String url;
	private int regport;
	private int port;
	
	public ConfigData (String h, int p, int p2, int r, int c, HashMap<String, HashSet<String>> p2f, HashMap<String, String> p2l, String idata) {
		this.url = h;
		this.port = p;
		this.regport = p2;
		this.repfactor = r;
		this.chunksize = c;
		this.part2file = p2f;
		this.part2loc = p2l;
		this.initData = idata;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public int getRegPort(){
		return this.regport;
	}
	
	public String getHost() {
		return this.url;
	}
	
	public int getRep() {
		return this.repfactor;
	}
	
	public int getChunk() {
		return this.chunksize;
	}
	
	public String getInitData() {
		return this.initData;
	}
	
	public HashMap<String, HashSet<String>> getP2F () {
		return this.part2file;
	}
	
	public HashMap<String, String> getP2L () {
		return this.part2loc;
	}
}
