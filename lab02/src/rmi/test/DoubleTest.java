package rmi.test;

import java.io.Serializable;

import rmi.com.Remote440;

public class DoubleTest implements Remote440, Serializable{
	private int state;
	
	public DoubleTest(int i){
		this.state = i;
	}
	
	public void voidWithSideEffects(FieldClass f, DoubleTest t) throws Exception{
		state = f.getState() + t.getStateAndAdd(0) + this.getStateAndAdd(0);
	}
	
	public int getStateAndAdd(Integer i) throws Exception {
		return state + i;
	}
}
