package kmeans.main;

import mpi.*;

import java.util.ArrayList;

import kmeans.type.DataType;
import kmeans.type.Group;
import kmeans.type.Point;
import kmeans.type.ReqObj;
import kmeans.type.ReqType;
import kmeans.type.RetObj;
import kmeans.func.*;

public class Slave {
	public static void runSlave() throws Exception{
		int rank = MPI.COMM_WORLD.Rank();
		while(true){
			Object[] message = new Object[1];
			MPI.COMM_WORLD.Recv(message, 0, 1, MPI.OBJECT, 0, MPI.ANY_TAG);
			ReqObj request = (ReqObj) message[0];
			ReqType rType = request.getReq();
			if(rType == ReqType.TERM) break;
			else {
				DataType dType = request.getType();
				if (rType == ReqType.ASSOC) {
					if(dType == DataType.POINT) {
						ArrayList<Point> means = request.getPointMeans();
						ArrayList<Point> points = request.getPoints();
						RetObj[] buf = new RetObj[1];
						buf[0] = new RetObj(rType, dType, Calcs.assocPoints(points, means));
						MPI.COMM_WORLD.Send(buf, 0, 1, MPI.OBJECT, 0, rank);
					} else if (dType == DataType.DNA) {
						ArrayList<String> dna = request.getDNA();
						ArrayList<String> means = request.getDNAMeans();
						RetObj[] buf = new RetObj[1];
						buf[0] = new RetObj(rType, dType, Calcs.assocDNA(dna, means));
						MPI.COMM_WORLD.Send(buf, 0, 1, MPI.OBJECT, 0, rank);
					}
				} else if (rType == ReqType.RECALC) {
					if(dType == DataType.POINT) {
						ArrayList<Point> means = request.getPointMeans();
						ArrayList<Group<Point>> gPoints = request.getGroupedPoints();
						RetObj[] buf = new RetObj[1];
						buf[0] = new RetObj(rType, dType, Calcs.recalculateMeans(means, gPoints));
						MPI.COMM_WORLD.Send(buf, 0, 1, MPI.OBJECT, 0, rank);
					} else if (dType == DataType.DNA) {
						ArrayList<Group<String>> gDNA = request.getGroupedDNA();
						ArrayList<String> means = request.getDNAMeans();
						RetObj[] buf = new RetObj[1];
						buf[0] = new RetObj(rType, dType, Calcs.recalculateDNA(means, gDNA));
						MPI.COMM_WORLD.Send(buf, 0, 1, MPI.OBJECT, 0, rank);
					}
				}
			}
		}
	}
}
