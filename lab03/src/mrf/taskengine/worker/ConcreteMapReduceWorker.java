package mrf.taskengine.worker;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

import mrf.taskengine.master.CommonObjects;
import mrf.taskengine.master.SerializableCallable;

public class ConcreteMapReduceWorker implements RemoteMapReduceWorker {
	
	private int                         cores = Runtime.getRuntime().availableProcessors();
	private HashMap<String, Future<Object>>         futures;
	private WorkerSettings              settings;
	private ExecutorService             threadpool;
	
	public ConcreteMapReduceWorker(WorkerSettings settings){
		this.settings   = settings;
		this.threadpool = Executors.newFixedThreadPool(cores);
		this.futures    = new HashMap<String, Future<Object>>();
	}
	
	public boolean addTask(SerializableCallable c){
		String mhost = settings.getMasterHost();
		int    mport = settings.getMasterPort();
		
		try {
			Socket sock  = new Socket(mhost, mport);
			OutputStream ostream = sock.getOutputStream();
			ObjectOutputStream oostream = new ObjectOutputStream(ostream);
			
			oostream.writeObject(CommonObjects.RequestType.ADD_TASK);
			oostream.writeObject(c);
			
			oostream.close();
			ostream.close();
			sock.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
		
	public boolean queueTask(String s, SerializableCallable c){
		if(c == null) return false;
			
		Future<Object> f = threadpool.submit(c);
		futures.put(s, f);
		
		return true;
	}
	
	public HashMap<String, Object> yield(){
		HashMap<String, Object> retv = new HashMap<String, Object>();
		Set<String>             done = new HashSet<String>();
		
		for (String s : futures.keySet()){
			Future<Object> f = futures.get(s);
			if(f.isDone()){
				done.add(s);
				try {
					retv.put(s, f.get());
				} catch (Exception e) {
					//TODO: handle exception here
					e.printStackTrace();
				}
			}
		}
		
		for (String s : done){
			futures.remove(s);
		}
		
		return retv;
	}
	
	public Set<String> poll(){
		Set<String> dead = new HashSet<String>();
		
		for(String s : futures.keySet()){
			Future<Object> f = futures.get(s);
			if(f.isCancelled()) dead.add(s);
		}
		
		for(String s : dead){
			futures.remove(s);
		}
		
		return dead;
	}
}
