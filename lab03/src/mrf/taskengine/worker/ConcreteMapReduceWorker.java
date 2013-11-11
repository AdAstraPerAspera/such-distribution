package mrf.taskengine.worker;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import mrf.taskengine.master.CommonObjects;
import mrf.taskengine.master.SerializableRunnable;

public class ConcreteMapReduceWorker implements RemoteMapReduceWorker {
	
	private Queue<SerializableRunnable> tasks;
	private WorkerSettings settings;
	
	public ConcreteMapReduceWorker(WorkerSettings settings){
		this.tasks = new LinkedList<SerializableRunnable>();
		this.settings = settings;
	}
	
	public boolean addTask(SerializableRunnable s){
		String mhost = settings.getMasterHost();
		int    mport = settings.getMasterPort();
		
		try {
			Socket sock  = new Socket(mhost, mport);
			OutputStream ostream = sock.getOutputStream();
			ObjectOutputStream oostream = new ObjectOutputStream(ostream);
			
			oostream.writeObject(CommonObjects.RequestType.ADD_TASK);
			oostream.writeObject(s);
			
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
	
	public void queueTask(SerializableRunnable s){
		tasks.add(s);
	}
}
