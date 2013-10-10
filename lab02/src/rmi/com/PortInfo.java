package rmi.com;

/**
 * Constants used for network IO
 * 
 * @author Michael Wang - mhw1
 */
public class PortInfo {
	/**
	 * Two variables to define the range of ports available for use.
	 * MAX_PORT must be between 0 and 65535, inclusive.
	 * MIN_PORT must be between 0 and MAX_PORT, inclusive.
	 */
	protected static final int MIN_PORT = 15440;
	protected static final int MAX_PORT = 15440;
	protected static final int NUM_SOCK = MAX_PORT - MIN_PORT + 1;
}
