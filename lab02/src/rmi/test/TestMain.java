package rmi.test;

import java.lang.reflect.*;

import com.sun.org.apache.bcel.internal.util.Class2HTML;

public class TestMain {
	private class Foo{
		public int sum(int... a){
			int ret = 0;
			for(int i : a){
				ret += i;
			}
			return ret;
		}
	}
	
	private class Bar extends Foo{
		public int mul(int... a){
			int ret = 1;
			for(int i : a){
				ret *= i;
			}
			return ret;
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
		
		Method sum = clazzB.getMethods()[0];
		Method mul = clazzF.getMethods()[0];
		
		System.out.println(sum);
		System.out.println(mul);
	}
}
