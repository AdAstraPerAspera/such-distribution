package kmeans.sequential;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import kmeans.func.Calcs;
import kmeans.type.*;

public class KMeansMain {
	private static void printHelp(){
	    System.out.println("Need arguments to run:");
    	System.out.println("If using DNA: use arguments \"-d <length of strings> <input file path> <# of clusters> <epsilon>\"");
    	System.out.println("If using points: use arguments \"-p <input file path> <# of clusters> <epsilon>\"");
    	System.exit(0);
  	}
	
	public static void main(String[] args) throws Exception{
		//process input
		DataType type      			  = null;
		int 				length	  = 0;
		int                 clusters = 0;
		double              eps = 0.0;
		ArrayList<String>   dnaData   = new ArrayList<String>();
		ArrayList<Point>    pointData = new ArrayList<Point>();
		
		if(args.length < 1) {
			KMeansMain.printHelp();
		} else if (args[0].equals("-d")){
			type = DataType.DNA;
			if(args.length < 5){
				KMeansMain.printHelp();
			}
			length = Integer.parseInt(args[1]);
			clusters = Integer.parseInt(args[3]);
			eps = Double.parseDouble(args[4]);
			
			FileInputStream   istream = new FileInputStream(args[2]);
			BufferedReader    reader  = new BufferedReader(new InputStreamReader(istream));
			String            line;
			
			while((line = reader.readLine()) != null){
				dnaData.add(line);
			}
			
			reader.close();
		} else if (args[0].equals("-p")){
			type = DataType.POINT;
			if(args.length < 4){
				KMeansMain.printHelp();
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
			KMeansMain.printHelp();
		}
		
		if(type == DataType.DNA){
			
		} else if(type == DataType.POINT){
			Calcs.pointMeans(clusters, pointData, eps);
		} else {
			System.err.println("Execution should never reach here.");
			System.exit(-1);
		}
	}
}
