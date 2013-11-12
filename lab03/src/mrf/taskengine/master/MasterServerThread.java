package mrf.taskengine.master;

import java.io.IOException;
import java.net.*;

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
			try {
				Socket sock = ssock.accept();
				sock.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
