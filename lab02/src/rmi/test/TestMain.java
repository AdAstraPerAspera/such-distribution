package rmi.test;

import java.io.File;

import rmi.com.RMIServer;
import rmi.com.ServerTask;
import rmi.reg.LocateRegistry;
import rmi.reg.ReferenceObject;

public class TestMain {
	public static void main(String[] args) throws Exception{
		ServerTask task = new ServerTask("localhost", 15441, "localhost", 15550);
		
		Thread t = new Thread(task);
		
		t.start();
		
		DoubleTest dt  = new DoubleTest(10);
		DoubleTest dt2 = new DoubleTest(10);
		
		Thread.sleep(5);
		
		//Replace the filepath with the appropriate value when testing
		RMIServer.bind("doubletest", dt, new File("./bin/rmi/test/").toURI().toURL());
		RMIServer.bind("doubletest2", dt2, new File("./bin/rmi/test/").toURI().toURL());
	}
}
