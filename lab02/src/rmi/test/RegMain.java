package rmi.test;

import rmi.reg.LocateRegistry;

public class RegMain {
	public static void main(String[] args){
		LocateRegistry.createRegistry(15550);
	}
}
