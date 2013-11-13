package mrf.taskengine.master;

import java.io.*;
import java.net.*;
import java.util.UUID;

import mrf.taskengine.worker.NodeInformation;

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
					if(node == "self"){
						state.runTask(n, t);
					} else {
						NodeInformation i = state.getNodeInfo(node);
						Socket outsock = new Socket(i.getHostname(), i.getPort());
						ostream = outsock.getOutputStream();
						
						ObjectOutputStream oostream = new ObjectOutputStream(ostream);
						
						oostream.writeObject(CommonObjects.RequestType.RUN_TASK);
						oostream.writeObject(n);
						oostream.writeObject(t);
						
						ostream.close();
						outsock.close();
					}
					break;
				case ADD_NODE:
					String s = (String) oistream.readObject();
					NodeInformation i = (NodeInformation) oistream.readObject();
					state.addNode(s, i);
					break;
				case RUN_TASK:
					//Code here should never be executed.
					System.err.println("Error: invalid request received");
				}
				sock.close();
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
