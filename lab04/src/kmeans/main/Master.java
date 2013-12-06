package kmeans.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import kmeans.type.DataType;
import kmeans.type.Point;

public class Master {
	public static void runMaster(String[] args, int size) throws Exception{
		//set up data and stuff
		DataType type      			  = null;
		int 				length	  = 0;
		int                 clusters  = 0;
		double              eps       = 0.0;
		ArrayList<String>   dnaData   = new ArrayList<String>();
		ArrayList<Point>    pointData = new ArrayList<Point>();
		
		for(int rank = 1; rank < size; rank++){
			if(args.length < 1) {
				MainClass.printHelp();
				//TODO: send message to children to terminate
				return;
			} else if (args[0].equals("-d")){
				type = DataType.DNA;
				if(args.length < 5){
					MainClass.printHelp();
					//TODO: send message to children to terminate
					return;
				}
				length = Integer.parseInt(args[1]);
				clusters = Integer.parseInt(args[3]);
				eps = Double.parseDouble(args[4]);
				
				FileInputStream   istream = new FileInputStream(args[2]);
				BufferedReader    reader  = new BufferedReader(new InputStreamReader(istream));
				String            line;
				
				while((line = reader.readLine()) != null){
					if(length == 0) {
						length = line.length();
					}
					
					if(line.length() != length){
						break;
					}
					
					dnaData.add(line);
				}
				
				reader.close();
			} else if (args[0].equals("-p")){
				type = DataType.POINT;
				if(args.length < 4){
					MainClass.printHelp();
					//TODO: send message to children to terminate
					return;
				}
				clusters = Integer.parseInt(args[2]);
				eps = Double.parseDouble(args[3]);
				FileInputStream   istream = new FileInputStream(args[1]);
				BufferedReader    reader  = new BufferedReader(new InputStreamReader(istream));
				String            line;
				
				while((line = reader.readLine()) != null){
					String[] points = line.split(",");
					Point temp = new Point(Double.parseDouble(points[0]), Double.parseDouble(points[1]));
					
					pointData.add(temp);
				}
				
				reader.close();
			} else {
				MainClass.printHelp();
				//TODO: send message to children to terminate
				return;
			}
		}
		
		//TODO: send messages and shit
		while(true){
			
		}
	}
}
