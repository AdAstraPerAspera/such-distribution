package mrf.taskengine.master;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;

import mrf.config.ConfigParser;

public class MasterServerMain {
	public static void main(String[] args){
		try {
			MasterSettings setting = new MasterSettings(ConfigParser.parse(new FileInputStream(new File(args[0]))));
			int 		   startpt = Integer.parseInt(setting.getValue("masterport"));
			int			   portnum = Integer.parseInt(setting.getValue("socketnum"));
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
		
		//TODO: poll on an interval
		//TODO: Once all results from a Map tasks are in, retrieve Reduce
	}
}
