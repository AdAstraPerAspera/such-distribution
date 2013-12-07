package kmeans.type;

import java.io.Serializable;
import java.util.ArrayList;

public class ReqObj implements Serializable {

	private ReqType query;
	private DataType type;
	private ArrayList<Point> pMeans;
	private ArrayList<String> dnaMeans;
	private ArrayList<Group<Point>> pGroups;
	private ArrayList<Group<String>> dnaGroups;
	private ArrayList<Point> points;
	private ArrayList<String> dna;
	
	public ReqType getReq() { return this.query; }
	
	public DataType getType() { return this.type; }
	
	public ArrayList<Point> getPointMeans() { return this.pMeans; }
	
	public ArrayList<String> getDNAMeans() { return this.dnaMeans; }
	
	public ArrayList<Group<Point>> getGroupedPoints() { return this.pGroups; }
	
	public ArrayList<Group<String>> getGroupedDNA() { return this.dnaGroups; }
	
	public ArrayList<Point> getPoints() { return this.points; }
	
	public ArrayList<String> getDNA() { return this.dna; }
	
	public ReqObj() {
		this.query = ReqType.TERM;
	}
	
	public ReqObj (ReqType r, DataType t, ArrayList m, ArrayList data) {
		this.query = r;
		this.type = t;
		if(r == ReqType.ASSOC){
			if (t == DataType.DNA){
				this.dnaMeans = (ArrayList<String>) m;
				this.dna = (ArrayList<String>) data;
			}
			else if (t == DataType.POINT) {
				this.pMeans = (ArrayList<Point>) m;
				this.points = (ArrayList<Point>) data;
			}
		}
		else if (r == ReqType.RECALC){
			if(t == DataType.DNA) {
				this.dnaMeans = (ArrayList<String>) m;
				this.dnaGroups = (ArrayList<Group<String>>) data;
			}
			else if (t == DataType.POINT) {
				this.pMeans = (ArrayList<Point>) m;
				this.pGroups = (ArrayList<Group<Point>>) data;
			}
		}
		else {
			this.query = ReqType.TERM;
		}
	}
}
