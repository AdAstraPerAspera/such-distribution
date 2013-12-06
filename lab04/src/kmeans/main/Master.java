package kmeans.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import kmeans.type.DataType;
import kmeans.type.Point;
import kmeans.type.ReqObj;

public class Master {
	public static void termAll(int size){
		ReqObj termReq = new ReqObj();
		ReqObj[] buf = new ReqObj[1];
		buf[0] = termReq;
		
		for(int i = 1; i < size; i++){
			MPI.COMM_WORLD.Send(buf, 0, 1, MPI.OBJECT, i, size);
		}
	}
	
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
				termAll(size);
				return;
			} else if (args[0].equals("-d")){
				type = DataType.DNA;
				if(args.length < 5){
					MainClass.printHelp();
					termAll(size);
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
					termAll(size);
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
				termAll(size);
				return;
			}
		}
		
		//TODO: send messages and shit
		while(true){
			if(type == DataType.DNA){
				//work with strings
				
				//split initial data into chunks to be processed
				ArrayList<ArrayList<String>> chunks = new ArrayList<ArrayList<String>>();
				for(int i = 0; i < size; i++){
					chunks.add(new ArrayList<String>());
				}
				
				for(int i = 0; i < dnaData.size(); i ++){
					chunks.get(i % size).add(dnaData.get(i));
				}
			} else {
				//work with points
			}
		}
	}
}
