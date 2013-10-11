package rmi.test;

import rmi.com.RMIInvocation;
import rmi.com.Stub;
import rmi.reg.ReferenceObject;

public class DoubleTest_Stub extends DoubleTest implements Stub{

	ReferenceObject ref;
	
	public DoubleTest_Stub() {
		super(0);
	}

	@Override
	public ReferenceObject getRef() {
		return ref;
	}

	@Override
	public void setROR(ReferenceObject ror) {
		this.ref = ror;		
	}
	
	@Override
	public void voidWithSideEffects(FieldClass f, DoubleTest t) throws Exception{
		RMIInvocation.invoke("voidWithSideEffects", ref.getType(), ref, f, t);
	}
	
	@Override
	public int getStateAndAdd(Integer i) throws Exception{
		return (int) RMIInvocation.invoke("getStateAndAdd", ref.getType(), ref, i);
	}

}
