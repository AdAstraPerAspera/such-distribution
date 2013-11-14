package mrf.config;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ConfigParser {
	public static HashMap<String,String> parse(FileInputStream f) throws IOException{
		HashMap<String,String> map = new HashMap<String,String>();
		
		InputStreamReader r = new InputStreamReader(f);
		BufferedReader br = new BufferedReader(r);
		
		String s = br.readLine();
		
		while(s != null){
			System.out.println(s);
			String[] split = s.split("=");
			
			try {
				map.put(split[0].toUpperCase(), split[1]);
				System.out.println(split[0]);
				System.out.println(split[1]);
			} catch (ArrayIndexOutOfBoundsException e){
				throw new IOException("Unexpected file format!\nMake sure all lines in the config file are of the form PARAMETER=value.");
			}
			
			s = br.readLine();
		}
		
		return map;
	}
}
