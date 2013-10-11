package rmi.test;

import java.io.File;

import rmi.com.RMIServer;
import rmi.com.ServerTask;
import rmi.reg.LocateRegistry;
import rmi.reg.ReferenceObject;

/**
 * Runs the RMI Server and binds two objects objects to it.
 * Takes two arguments on the command line: the port for the RMI server to listen to, and the port that the registry listens to on localhost.
 * 
 * @author Michael Wang - mhw1
 */
public class TestMain {
	public static void main(String[] args) throws Exception{
		ServerTask task = new ServerTask("localhost", Integer.parseInt(args[0]), "localhost", Integer.parseInt(args[1]));
		
		System.out.println(LocateRegistry.hasRegistry("localhost", Integer.parseInt(args[1])));
		
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
