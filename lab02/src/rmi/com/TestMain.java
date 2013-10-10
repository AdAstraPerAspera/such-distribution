package rmi.com;

import java.lang.reflect.*;

import com.sun.org.apache.bcel.internal.util.Class2HTML;

public class TestMain {
	private class Foo{
		public int sum(int a, int b){
			return a + b;
		}
	}
	
	private class Bar extends Foo{
		public int pow(int a){
			return a * a;
		}
	}
	
	public static void main(String[] args){
		TestMain t = new TestMain();
		Bar b = t.new Bar();
		Foo f = (Foo) b;
		
		Class clazzB = b.getClass();
		Class clazzF = f.getClass();
		
		System.out.println(clazzB);
		System.out.println(clazzF);
	}
}
