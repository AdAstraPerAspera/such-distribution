package mrf.tasks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import mrf.dfs.MRFile;

abstract public class AbstractMapCallable<U, T> implements MapCallable<U, T>{

	@Override
	final public void map(MRFile fileIn, String fileOut) {
		//pull data from file and cast to correct type
		ArrayList<Object> raw = fileIn.getContents();
		ArrayList<U> data = new ArrayList<U>();
		for(Object o : raw){
			data.add((U) o);
		}
		
		//perform map operation
		ArrayList<T> output = new ArrayList<T>();
		
		for(U in : data){
			output.add(map(in));
		}
		
		//write out results to temporary file
		try {
			File file = new File(fileOut);
			file.mkdirs();
			file.createNewFile();
			FileOutputStream fos   = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(output);
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
