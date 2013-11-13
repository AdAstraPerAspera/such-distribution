package mrf.taskengine.master;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;

import mrf.config.ConfigParser;

public class MasterServerMain {
	public static void main(String[] args){
		int            startpt = Integer.parseInt(args[0]);
		int            portnum = Integer.parseInt(args[1]);
		try {
			MasterSettings setting = new MasterSettings(ConfigParser.parse(new FileInputStream(new File(args[2]))));
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
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			System.err.println("Error: config file not found!");
		} catch (IOException e1) {
			e1.printStackTrace();
			System.err.println("Error accessing config file!");
		}
	}
}
