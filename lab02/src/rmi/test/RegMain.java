package rmi.test;

import rmi.reg.LocateRegistry;

/**
 * Starts the registry.  Takes the port to listen to as an argument on the command line.
 * 
 * @author Michael Wang - mhw1
 */
public class RegMain {
	public static void main(String[] args){
		LocateRegistry.createRegistry(Integer.parseInt(args[0]));
	}
}
