package rmi.test;

import rmi.com.Stub;
import rmi.reg.ReferenceObject;

public class DoubleTest_Stub extends DoubleTest implements Stub{

	ReferenceObject ref;
	
	public DoubleTest_Stub(ReferenceObject ref) {
		super(0);
		this.ref = ref;
	}

	@Override
	public ReferenceObject getRef() {
		return ref;
	}

}
