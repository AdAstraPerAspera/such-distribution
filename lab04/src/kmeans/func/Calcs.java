package kmeans.func;

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
}
