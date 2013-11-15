package mrf.config;

import java.util.HashMap;

/**
 * Abstract implementation of Settings
 * 
 * @author Michael Wang - mhw1
 *
 */
public abstract class AbstractSettings implements Settings{
	private HashMap<String,String> settings;
	
	public AbstractSettings(HashMap<String,String> settings){
		this.settings = settings;
	}
	
	@Override
	public String getValue(String s){
		return settings.get(s.toUpperCase());
	}
	
	@Override
	public boolean setValue(String k, String v){
		try {
			settings.put(k, v);
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	
}
