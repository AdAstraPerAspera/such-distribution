package mrf.dfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import mrf.config.ConfigParser;

public class DFSCoordinator {

	HashMap<String, HashSet<String>> part2file;
	HashMap<String, HashSet<String>> file2part;
	HashMap<String, String> part2loc;
	HashMap<String, String> loc2part;
	int repfactor;
	
	
	/*
	 * TODO: Parse config
	 * 
	 * TODO: Send config to participants
	 * 
	 * TODO: Replicate files among participants
	 * TODO: Track Replication
	 * TODO: Mark participants as active or down
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
					String tmploc = parsed.get(s);
					this.part2file.put(s, files);
					this.part2loc.put(s, tmploc);
					this.loc2part.put(tmploc, s);
				}
			}
			
			if (this.repfactor <= 0) {
				throw new Exception("Failed to parse a valid replication factor from the config file");
			}
			else {
				this.repfactor = (this.repfactor <= this.part2file.size()) ? this.repfactor : this.part2file.size();
			}
			
			for (String p : this.part2loc.keySet()) {
				/*
				 * TODO: Send the config file to each participant
				 */
			}
			
			File data = new File(initData);
			File[] dataFiles = data.listFiles();
			for (File f: dataFiles) {
				/* 
				 * TODO: Figure out distributing scheme in here, specifically how to send the files.
				 */
				for(int i = 0; i < this.repfactor; i++){
					// For the multiple copies
					continue;
				}
			}

			
		} catch (FileNotFoundException e) {
			System.err.println("Invalid Config File Path: " + e);
		} catch (IOException e) {
			System.err.println("IOException while parsing config: " + e);
		} catch (Exception e) {
			System.err.println("Exception: " + e);
		}
	}
}
