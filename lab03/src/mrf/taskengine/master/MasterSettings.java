package mrf.taskengine.master;

import java.util.HashMap;

import mrf.config.AbstractSettings;

public class MasterSettings extends AbstractSettings {

	public MasterSettings(HashMap<String, String> settings) {
		super(settings);
		// TODO Auto-generated constructor stub
	}
	
	public String getMasterHost(){
		return getValue("masterhost");
	}
	
	public int getMasterPort(){
		return Integer.getInteger(getValue("masterport"));
	}
}
