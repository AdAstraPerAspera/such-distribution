package rmi.test;

import rmi.com.Remote440;

public class DoubleTest implements Remote440 {
	private int state;
	
	public DoubleTest(int i){
		this.state = i;
	}
	
	public void voidWithSideEffects(FieldClass f, DoubleTest t){
		f.setState(f.getState() + t.getState() + this.getState());
	}
	
	public int getState(){
		return state;
	}
}
