package rmi.test;

public class FieldClass {
	private int state;
	
	public FieldClass(int i){
		state = i;
	}
	
	public int getState(){
		return state;
	}
	
	public void setState(int i){
		state = i;
	}
}
