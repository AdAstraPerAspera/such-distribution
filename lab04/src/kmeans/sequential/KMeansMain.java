package kmeans.sequential;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class KMeansMain {
	private enum DataType{
		DNA, POINT;
	}
	
	private static class Point{
		double x, y;
		
		public Point(double x, double y){
			this.x = x;
			this.y = y;
		}
		
		public double getX() { return x; }
		
		public double getY() { return y; }
	}
	
	private static void printHelp(){
	  System.out.println("Need arguments to run:");
    	System.out.println("If using DNA: use arguments \"-d <length of strings> <input file path>\"");
    	System.out.println("If using points: use arguments \"-p <input file path>\"");
    	System.exit(0);
  	}

	private static double cartesianDistance(Point x, Point y){
		return 0.0;
	}
	
	public static void main(String[] args) throws Exception{
		//process input
		KMeansMain.DataType type      = null;
		int 				length	  = 0;
		ArrayList<String>   dnaData   = new ArrayList<String>();
		ArrayList<Point>    pointData = new ArrayList<Point>();
		
		if(args.length < 1) {
			KMeansMain.printHelp();
		} else if (args[0].equals("-d")){
			type = KMeansMain.DataType.DNA;
			if(args.length < 3){
				KMeansMain.printHelp();
			}
			length = Integer.parseInt(args[1]);
			
			FileInputStream   istream = new FileInputStream(args[2]);
			BufferedReader    reader  = new BufferedReader(new InputStreamReader(istream));
			String            line;
			
			while((line = reader.readLine()) != null){
				dnaData.add(line);
			}
			
			reader.close();
		} else if (args[0].equals("-p")){
			type = KMeansMain.DataType.POINT;
			if(args.length < 2){
				KMeansMain.printHelp();
			}
			FileInputStream   istream = new FileInputStream(args[1]);
			BufferedReader    reader  = new BufferedReader(new InputStreamReader(istream));
			String            line;
			
			while((line = reader.readLine()) != null){
				String[] points = line.split(",");
				Point temp = new KMeansMain.Point(Double.parseDouble(points[0]), Double.parseDouble(points[1]));
				
				pointData.add(temp);
			}
			
			reader.close();
		} else {
			KMeansMain.printHelp();
		}
		
		if(type == KMeansMain.DataType.DNA){
			
		} else if(type == KMeansMain.DataType.POINT){
			
		} else {
			System.err.println("Execution should never reach here.");
			System.exit(-1);
		}
	}
}
