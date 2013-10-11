package rmi.test;

import rmi.com.Stub;
import rmi.reg.LocateRegistry;
import rmi.reg.ReferenceObject;

/**
 * Tests remote function invocation.  Takes the port that the registry
 * listens on as a command-line argument.
 * 
 * @author Michael Wang - mhw1
 */
public class TestMain2 {
	public static void main(String[] args){
		ReferenceObject ref  = LocateRegistry.registryLookup("doubletest", "localhost", Integer.parseInt(args[0]));
		ReferenceObject ref2 = LocateRegistry.registryLookup("doubletest2", "localhost", Integer.parseInt(args[0]));
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
