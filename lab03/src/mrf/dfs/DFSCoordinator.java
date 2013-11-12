package mrf.dfs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import mrf.config.ConfigParser;

public class DFSCoordinator {

	HashMap<String, HashSet<String>> part2file;
	HashMap<String, HashSet<String>> file2part;
	HashMap<String, String> partLoc;
	int repfactor;
	
	
	/*
	 * TODO: Parse config
	 * 
	 * TODO: Send config to participants
	 * 
	 * TODO: Replicate files among participants
	 * TODO: Track Replication
	 * TODO: Mark participants as active of down
	 * 
	 * TODO: Send a copy of a file from one node to another if requested
	 * 
	 * TODO: Threads
	 */
	
	
	public DFSCoordinator (String configPath, String initData) throws Exception {
		try {
			FileInputStream fis = new FileInputStream(configPath);
			HashMap<String, String> parsed = ConfigParser.parse(fis);
			fis.close();
			
			for(String s: parsed.keySet()) {
				if(s.equals("factor")) {
					this.repfactor = Integer.parseInt(parsed.get("factor"));
				}
				else {
					HashSet<String> files = new HashSet();
					this.part2file.put(s, files);
					this.partLoc.put(s, parsed.get(s));
				}
			}
			if (this.repfactor <= 0) {
				throw new Exception("Failed to parse a valid replication factor from the config file");
			}
			
			

			
		} catch (FileNotFoundException e) {
			System.err.println("Invalid Config File Path: " + e);
		} catch (IOException e) {
			System.err.println("IOException while parsing config: " + e);
		}
	}
}
