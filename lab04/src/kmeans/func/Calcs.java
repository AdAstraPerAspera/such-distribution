package kmeans.func;

import kmeans.type.Point;

public class Calcs {
	public static double cartesianDistance(Point p, Point q){
		return Math.sqrt(Math.pow(p.getX()-q.getX(), 2.0) + Math.pow(p.getY() - q.getY(),2.0));
	}
	
}
