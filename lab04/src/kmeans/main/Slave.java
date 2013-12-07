package kmeans.main;

import java.util.ArrayList;

import kmeans.type.DataType;
import kmeans.type.Group;
import kmeans.type.Point;
import kmeans.type.ReqObj;
import kmeans.type.ReqObj.ReqType;
import kmeans.func.*;

public class Slave {
	public static void runSlave(){
		while(true){
			Object[] message = new Object[1];
			MPI.COMM_WORLD.Recv(message, 0, 1, MPI.OBJECT, 0, MPI.ANY_TAG);
			ReqObj request = (ReqObj) message[1];
			ReqType rType = request.getReq();
			if(rType == ReqType.TERM) break;
			else {
				DataType dType = request.getType();
				if (rType == ReqType.ASSOC) {
					if(dType == DataType.POINT) {
						ArrayList<Point> means = request.getPointMeans();
						ArrayList<Point> points = request.getPoints();
						Calcs.assocPoints(points, means);
					} else if (dType == DataType.DNA) {
						ArrayList<String> dna = request.getDNA();
						ArrayList<String> means = request.getDNAMeans();
						Calcs.assocDNA(dna, means);
					}
				} else if (rType == ReqType.RECALC) {
					if(dType == DataType.POINT) {
						ArrayList<Point> means = request.getPointMeans();
						ArrayList<Group<Point>> gPoints = request.getGroupedPoints();
						Calcs.recalculateMeans(means, gPoints);
					} else if (dType == DataType.DNA) {
						ArrayList<Group<String>> gDNA = request.getGroupedDNA();
						ArrayList<String> means = request.getDNAMeans();
						Calcs.recalculateDNA(means, gDNA);
					}
				}
			}
		}
		MPI.Finalize();
	}
}
