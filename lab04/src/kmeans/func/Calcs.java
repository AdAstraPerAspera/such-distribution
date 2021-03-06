package kmeans.func;

import java.util.ArrayList;

import kmeans.type.Group;
import kmeans.type.Point;

/**
 * 
 * @author wmaynes
 *
 *
 * All calculations and work functions for the entire project - Calculating distances, means, and associating points.
 */

public class Calcs {
	public static double cartesianDistance(Point p, Point q){
		return Math.sqrt(Math.pow(p.getX()-q.getX(), 2.0) + Math.pow(p.getY() - q.getY(),2.0));
	}
	
	public static int dnaDistance (String A, String B) {
		int diffCount = 0;
		for(int i = 0; i < A.length(); i++) {
			if(!A.substring(i, i+1).equals(B.substring(i, i+1))) { diffCount++; }
		}
		return diffCount;
	}
	
	public static ArrayList<Group<Point>> assocPoints (ArrayList<Point>points, ArrayList<Point> means){
		ArrayList<Group<Point>> resPoints = new ArrayList<Group<Point>>();
		for(Point p : points){
			double minDist = Double.MAX_VALUE;
			Point minMean = null;
			for(Point m : means) {
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
			
			if(pointCount != 0){
				newMeans.add(new Point(xCoord / pointCount, yCoord / pointCount));
			} else {
				newMeans.add(m);
			}
		}
		return newMeans;
	}
	
	public static ArrayList<Point> pointMeans(int K, ArrayList<Point> pData, Double eps) {
		ArrayList<Point> means = new ArrayList<Point>();
		int partSize = pData.size()/K;
		for(int i = 0; i < K; i++){
			Point newMean = pData.get((int)(Math.random() * partSize) + (i * partSize));
			while(means.contains(newMean)){ newMean = pData.get((int)(Math.random() * partSize) + (i * partSize)); }
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
	
	public static ArrayList<Group<String>> assocDNA (ArrayList<String> dna, ArrayList<String> means) {
		ArrayList<Group<String>> groupDNA = new ArrayList<Group<String>>();
		for (String d : dna) {
			int minDist = Integer.MAX_VALUE;
			String minMean = null;
			for(String m : means) {
				int dist = Calcs.dnaDistance(d, m);
				if (dist < minDist) {
					minDist = dist;
					minMean = m;
				}
			}
			groupDNA.add(new Group<String>(d, minMean));
		}
		return groupDNA;
	}
	
	public static ArrayList<String> recalculateDNA(ArrayList<String> means, ArrayList<Group<String>> gDNA) {
		ArrayList<String> newMeans = new ArrayList<String> ();
		for (String m : means) {
			String newMean = new String();
			
			for(int i = 0; i < m.length(); i++){
				int aCount = 0;
				int cCount = 0;
				int gCount = 0;
				int tCount = 0;
				for(Group<String> g : gDNA) {
					if(m.equals(g.getMean())) {
						String dna = g.getVal();
						char c = dna.charAt(i);
						if (c == 'A') { aCount ++; }
						else if (c == 'C') { cCount ++; }
						else if (c == 'G') { gCount ++; }
						else if (c == 'T') { tCount ++; }
						else { System.err.println("lolwut. How'd I get here? Bad DNA bro."); }
					}
				}
				if (aCount >= cCount && aCount >= gCount && aCount >= tCount) { newMean = newMean.concat("A"); }
				else if (cCount >= aCount && cCount >= gCount && cCount >= tCount) { newMean = newMean.concat("C"); }
				else if (gCount >= aCount && gCount >= cCount && gCount >= tCount) { newMean = newMean.concat("G"); }
				else if (tCount >= aCount && tCount >= cCount && tCount >= gCount) { newMean = newMean.concat("T"); }
			}
			if(newMean.length() > 0){
				newMeans.add(newMean);
			} else {
				newMeans.add(m);
			}
		}
		return newMeans;
	}
		
	public static ArrayList<String> dnaMeans(int K, double eps, int n, ArrayList<String> dnaData) {
		ArrayList<String> means = new ArrayList<String>();
		int partSize = dnaData.size()/K;
		
		for(int i = 0; i < K; i++){
			String newMean = dnaData.get((int)(Math.random() * partSize) + (i * partSize));
			while(means.contains(newMean)) newMean = dnaData.get((int)(Math.random() * partSize) + (i * partSize));
			means.add(newMean);
		}
		double maxProportionChange = Double.MAX_VALUE;
		while (maxProportionChange > eps) {
			ArrayList<Group<String>> groupedDNA = assocDNA(dnaData, means);
			ArrayList<String> newMeans = recalculateDNA(means, groupedDNA);
			double maxChange = 0.0;
			for(int i = 0; i < newMeans.size(); i++) {
				double change = (Calcs.dnaDistance(means.get(i), newMeans.get(i)) / (n * 1.0));
				if (change > maxChange) { maxChange = change; }
			}
			means = newMeans;
			maxProportionChange = maxChange;
		}
		return means;
	}
}
