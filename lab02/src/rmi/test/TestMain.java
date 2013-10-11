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
		
		RMIServer.bind("doubletest", dt, new File("C:\\User\\Michael\\Documents\\GitHub\\such-distribution\\lab02\\bin\\rmi\\test\\").toURI().toURL());
		RMIServer.bind("doubletest2", dt2, new File("C:\\User\\Michael\\Documents\\GitHub\\such-distribution\\lab02\\bin\\rmi\\test\\").toURI().toURL());
	}
}
