package rmi.test;

import java.io.File;

import rmi.com.RMIServer;
import rmi.com.Stub;
import rmi.reg.LocateRegistry;
import rmi.reg.ReferenceObject;

public class TestMain2 {
	public static void main(String[] args){
		ReferenceObject ref  = LocateRegistry.registryLookup("doubletest", "localhost", 15550);
		ReferenceObject ref2 = LocateRegistry.registryLookup("doubletest2", "localhost", 15550);
		FieldClass f = new FieldClass(1);
		
		try {
			DoubleTest dt  = (DoubleTest) ref.localize();
			DoubleTest dt2 = (DoubleTest) ref.localize();
			System.out.println(dt.getStateAndAdd(4));
			dt.voidWithSideEffects(f, dt2);
			System.out.println(dt.getStateAndAdd(4));
			
			if(dt instanceof Stub){
				System.out.println(dt.getClass().getSuperclass());
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
