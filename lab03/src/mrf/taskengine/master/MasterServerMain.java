package mrf.taskengine.master;

import java.io.IOException;
import java.net.ServerSocket;

public class MasterServerMain {
	public static void main(String[] args){
		int            startpt = Integer.parseInt(args[0]);
		int            portnum = Integer.parseInt(args[1]);
		
		ServerSocket[] sockarr = new ServerSocket[portnum];
		int[]          portarr = new int[portnum];
		
		int upper  = sockarr.length;
		int offset = 0;
		for(int i = 0; i < upper; offset++){
			try {
				sockarr[i] = new ServerSocket(startpt + offset);
				portarr[i] = startpt + offset;
				i++;
				offset++;
			} catch (IOException e) {
				e.printStackTrace();
				offset++;
			}
		}
	}
}
