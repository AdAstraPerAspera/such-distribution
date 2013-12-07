package kmeans.type;

import java.io.Serializable;

public class Point implements Serializable{
	double x, y;
	
	public Point(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public boolean equals (Point p) {
		return (p.x == this.x) & (p.y == this.y);
	}
	
	public double getX() { return x; }
	
	public double getY() { return y; }
}
