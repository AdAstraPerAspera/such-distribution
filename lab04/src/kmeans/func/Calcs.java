package kmeans.func;

import java.util.ArrayList;

import kmeans.type.Group;
import kmeans.type.Point;

public class Calcs {
	public static double cartesianDistance(Point p, Point q){
		return Math.sqrt(Math.pow(p.getX()-q.getX(), 2.0) + Math.pow(p.getY() - q.getY(),2.0));
	}
	
	public static int dnaDistance (String A, String B) {
		int diffCount = 0;
		for (int i = 0; i < A.length(); i++) {
			if(A.substring(i, i+1).equals(B.substring(i, i+1))) { diffCount++; }
		}
		return diffCount;
	}
	
	public static ArrayList<Group<Point>> assocPoints (ArrayList<Point>points, ArrayList<Point>Means){
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
	
	public static ArrayList<Point> recalculateMeans(ArrayList<Point> means, ArrayList<Group<Point>> gPoints) {
		ArrayList<Point> newMeans = new ArrayList<Point>();
		for(Point m : means) {
			/* 
			 * Since we're picking our means from existing points we know we must have at least one point which is
			 * grouped to each point.
			 */
			int pointCount = 0;
			double xCoord = 0.0;
			double yCoord = 0.0;
			for(Group<Point> g : gPoints) {
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
	
	public static ArrayList<Point> pointMeans(int K, ArrayList<Point> pData, Double eps) {
		ArrayList<Point> means = new ArrayList<Point>();
		int partSize = pData.size()/K;
		for(int i = 0; i < partSize; i++){
			Point newMean = pData.get((int)(Math.random() * partSize));
			while(means.contains(newMean)) newMean = pData.get((int)(Math.random() * partSize));
			means.add(newMean);
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
}
