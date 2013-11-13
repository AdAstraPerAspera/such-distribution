package mrf.taskengine.master;

import java.util.HashMap;

import mrf.config.AbstractSettings;

/*
 * Config file format:
 * 
 * computenodes=<number>
 * masterport=<number>
 * socketnum=<number>
 */

public class MasterSettings extends AbstractSettings {

	public MasterSettings(HashMap<String, String> settings) {
		super(settings);
		// TODO Auto-generated constructor stub
	}
	
	public int getMasterPort(){
		return Integer.getInteger(getValue("masterport"));
	}
}
