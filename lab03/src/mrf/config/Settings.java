package mrf.config;

/**
 * Interface for a simple settings object
 * 
 * @author Michael Wang - mhw1
 * 
 */
public interface Settings {
	/**
	 * Get the value of a setting for a specific key
	 * @param s - key
	 * @return value of setting
	 */
	public String getValue(String s);
	
	/**
	 * Sets the value of a setting
	 * @param k - key
	 * @param v - value
	 * @return success of operation
	 */
	public boolean setValue(String k, String v);
}
