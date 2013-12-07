package kmeans.type;

import java.io.Serializable;
import java.util.ArrayList;

public class RetObj implements Serializable {
	private ReqType retType;
	private DataType type;
	private ArrayList<Group<Point>> pGroups;
	private ArrayList<Group<String>> dnaGroups;
	private ArrayList<Point> pMeans;
	private ArrayList<String> dnaMeans;
	
	public ArrayList<Point> getPointMeans() { return this.pMeans; }
	
	public ArrayList<String> getDNAMeans() { return this.dnaMeans; }
	
	public ArrayList<Group<Point>> getGroupedPoints() { return this.pGroups; }
	
	public ArrayList<Group<String>> getGroupedDNA() { return this.dnaGroups; }
	
	public RetObj (ReqType r, DataType t, ArrayList result) {
		this.retType = r;
		this.type = t;
		if(r == ReqType.RECALC){
			if (t == DataType.DNA){
				this.dnaMeans = (ArrayList<String>) result;
			}
			else if (t == DataType.POINT) {
				this.pMeans = (ArrayList<Point>) result;
			}
		}
		else if (r == ReqType.ASSOC){
			if(t == DataType.DNA) {
				this.dnaGroups = (ArrayList<Group<String>>) result;
			}
			else if (t == DataType.POINT) {
				this.pGroups = (ArrayList<Group<Point>>) result;
			}
		}
		else {
			this.retType = ReqType.TERM;
		}
	}

}
