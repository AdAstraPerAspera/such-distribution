package mrf.taskengine.master;

import java.io.*;
import java.net.*;
import java.util.UUID;

public class MasterServerThread implements Runnable {

	private MasterServerState state;
	private ServerSocket      ssock;
	
	public MasterServerThread(ServerSocket ssock, MasterServerState state){
		this.ssock = ssock;
		this.state = state;
	}
	
	@Override
	public void run() {
		while(true){
			Socket          sock = null;
			InputStream  istream = null;
			OutputStream ostream = null;
			try {
				sock = ssock.accept();
				istream = sock.getInputStream();
				
				ObjectInputStream oistream = new ObjectInputStream(istream);
				
				CommonObjects.RequestType req = (CommonObjects.RequestType) oistream.readObject();
				
				switch(req){
				case ADD_TASK:
					String n = UUID.randomUUID().toString();
					while(!state.addTask(n)) n = UUID.randomUUID().toString();
					SerializableCallable t = (SerializableCallable) oistream.readObject();
					String node = state.getScheduler().schedule(n);
					break;
				case ADD_NODE:
					String s = (String) oistream.readObject();
					state.addNode(s);
					break;
				}
				sock.close();
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
