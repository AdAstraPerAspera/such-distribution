package kmeans.sequential;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import kmeans.type.*;

public class KMeansMain {
	private static void printHelp(){
	    System.out.println("Need arguments to run:");
    	System.out.println("If using DNA: use arguments \"-d <length of strings> <input file path> <# of clusters>\"");
    	System.out.println("If using points: use arguments \"-p <input file path> <# of clusters>\"");
    	System.exit(0);
  	}
	
	private static ArrayList<Point> pointMeans(int K, ArrayList<Point> pData) {
		ArrayList<Point> means = new ArrayList<Point>();
		int partSize = pData.size()/K;
		for(int i = 0; i < partSize; i++){
			means.add(pData.get((int)(Math.random() * partSize)));
		}
		
		
		return pData;
	}
	
	public static void main(String[] args) throws Exception{
		//process input
		DataType type      			  = null;
		int 				length	  = 0;
		int                 clusters = 0;
		ArrayList<String>   dnaData   = new ArrayList<String>();
		ArrayList<Point>    pointData = new ArrayList<Point>();
		
		if(args.length < 1) {
			KMeansMain.printHelp();
		} else if (args[0].equals("-d")){
			type = DataType.DNA;
			if(args.length < 4){
				KMeansMain.printHelp();
			}
			length = Integer.parseInt(args[1]);
			clusters = Integer.parseInt(args[3]);
			
			FileInputStream   istream = new FileInputStream(args[2]);
			BufferedReader    reader  = new BufferedReader(new InputStreamReader(istream));
			String            line;
			
			while((line = reader.readLine()) != null){
				dnaData.add(line);
			}
			
			reader.close();
		} else if (args[0].equals("-p")){
			type = DataType.POINT;
			if(args.length < 3){
				KMeansMain.printHelp();
			}
			clusters = Integer.parseInt(args[2]);
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
			pointMeans(clusters, pointData);
		} else {
			System.err.println("Execution should never reach here.");
			System.exit(-1);
		}
	}
}
