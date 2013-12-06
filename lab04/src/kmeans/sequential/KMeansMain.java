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
	
	private static ArrayList<Group<Point>> assocPoints (ArrayList<Point>points, ArrayList<Point>Means){
		ArrayList<Group<Point>> resPoints = new ArrayList<Group<Point>>();
		for(Point p : points){
			double minDist = Double.MAX_VALUE;
			Point minMean = null;
			for(Point m : Means) {
				double currDist = Calcs.cartesianDistance(p, m);
				if(currDist < minDist) {
					minDist = currDist;
					minMean = m;
				}
			}
			resPoints.add(new Group<Point>(p, minMean));
		}
		return resPoints;
	}
	
	private static ArrayList<Point> recalculateMeans(ArrayList<Point> means, ArrayList<Group<Point>> gPoints) {
		ArrayList<Point> newMeans = new ArrayList<Point>();
		for(Point m : means) {
			/* 
			 * Since we're picking our means from existing points we know we must have at least one point which is
			 * grouped to each point.
			 */
			int pointCount = 0;
			double xCoord = 0.0;
			double yCoord = 0.0;
			for(Group g : gPoints) {
				if(m.equals(g.getMean())) {
					pointCount++;
					Point q = (Point) g.getVal();
					xCoord += q.getX();
					yCoord += q.getY();
				}
			}
			newMeans.add(new Point(xCoord / pointCount, yCoord / pointCount));
		}
		return newMeans;
	}
	
	private static ArrayList<Point> pointMeans(int K, ArrayList<Point> pData, Double eps) {
		ArrayList<Point> means = new ArrayList<Point>();
		int partSize = pData.size()/K;
		for(int i = 0; i < partSize; i++){
			means.add(pData.get((int)(Math.random() * partSize)));
		}
		double maxMeanChange = Double.MAX_VALUE;
		while (maxMeanChange > eps) {
			ArrayList<Group<Point>> groupedPoints = assocPoints(pData, means);
			ArrayList<Point> newMeans = recalculateMeans(means, groupedPoints);
			double maxChange = 0.0;
			for(int i = 0; i < newMeans.size(); i++) {
				double change = Calcs.cartesianDistance(means.get(i), newMeans.get(i));
				if (change > maxChange) { maxChange = change; }
			}
			means = newMeans;
			maxMeanChange = maxChange;
		}
		return means;
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
			pointMeans(clusters, pointData, eps);
		} else {
			System.err.println("Execution should never reach here.");
			System.exit(-1);
		}
	}
}
