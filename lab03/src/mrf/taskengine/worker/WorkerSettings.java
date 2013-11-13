package mrf.taskengine.worker;

import java.util.HashMap;

import mrf.config.AbstractSettings;

public class WorkerSettings extends AbstractSettings {

	public WorkerSettings(HashMap<String,String> settings){
		super(settings);
	}
	
	public String getMasterHost(){
		return getValue("masterhost");
	}
	
	public int getMasterPort(){
		return Integer.getInteger(getValue("masterport"));
	}
}
