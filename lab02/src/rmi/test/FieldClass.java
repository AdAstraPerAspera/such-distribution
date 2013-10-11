package rmi.test;

import java.io.Serializable;

public class FieldClass implements Serializable{
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
