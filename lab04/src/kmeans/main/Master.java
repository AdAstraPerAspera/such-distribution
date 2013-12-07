package kmeans.main;

import mpi.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import kmeans.func.Calcs;
import kmeans.type.DataType;
import kmeans.type.Group;
import kmeans.type.Point;
import kmeans.type.ReqObj;
import kmeans.type.ReqType;
import kmeans.type.RetObj;

public class Master {
	public static void termAll(int size){
		ReqObj termReq = new ReqObj();
		ReqObj[] buf = new ReqObj[1];
		buf[0] = termReq;
		
		for(int i = 1; i < size; i++){
			MPI.COMM_WORLD.Send(buf, 0, 1, MPI.OBJECT, i, size);
		}
	}
	
	public static void runMaster(String[] args, int size) throws Exception{
		//set up data and stuff
		DataType type      			  = null;
		int 				length	  = 0;
		int                 clusters  = 0;
		double              eps       = 0.0;
		double              change    = Double.MAX_VALUE;
		ArrayList<String>   dnaData   = new ArrayList<String>();
		ArrayList<Point>    pointData = new ArrayList<Point>();
		
		for(int rank = 1; rank < size; rank++){
			if(args.length < 1) {
				MainClass.printHelp();
				termAll(size);
				return;
			} else if (args[0].equals("-d")){
				type = DataType.DNA;
				if(args.length < 5){
					MainClass.printHelp();
					termAll(size);
					return;
				}
				length = Integer.parseInt(args[1]);
				clusters = Integer.parseInt(args[3]);
				eps = Double.parseDouble(args[4]);
				
				FileInputStream   istream = new FileInputStream(args[2]);
				BufferedReader    reader  = new BufferedReader(new InputStreamReader(istream));
				String            line;
				
				while((line = reader.readLine()) != null){
					if(length == 0) {
						length = line.length();
					}
					
					if(line.length() != length){
						break;
					}
					
					dnaData.add(line);
				}
				
				reader.close();
			} else if (args[0].equals("-p")){
				type = DataType.POINT;
				if(args.length < 4){
					MainClass.printHelp();
					termAll(size);
					return;
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
				MainClass.printHelp();
				termAll(size);
				return;
			}
		}
		
		//TODO: send messages and shit
		while(true){
			if(type == DataType.DNA){
				//work with strings
				
				//split initial data into chunks to be processed
				ArrayList<ArrayList<String>> chunks = new ArrayList<ArrayList<String>>();
				for(int i = 0; i < size; i++){
					chunks.add(new ArrayList<String>());
				}
				
				for(int i = 0; i < dnaData.size(); i++){
					chunks.get(i % size).add(dnaData.get(i));
				}
				
				ArrayList<String> means = new ArrayList<String>();
				int partSize = dnaData.size() / clusters;
				for(int i = 0; i < clusters; i++){
					String newMean = dnaData.get((int)(Math.random() * partSize) + i * partSize);
					while(means.contains(newMean)) newMean = dnaData.get((int)(Math.random() * partSize) + i * partSize);
					means.add(newMean);
				}
				
				while(change > eps){
					Request[]  reqs  = new Request[size];
					RetObj[][] resps = new RetObj[size][1];
					
					for(int i = 1; i < size; i++){
						ReqObj message = new ReqObj(ReqType.ASSOC, DataType.DNA, means, chunks.get(i));
						
						MPI.COMM_WORLD.Isend(message, 0, 1, MPI.OBJECT, i, size);
						
						reqs[i] = MPI.COMM_WORLD.Irecv(resps[i], 0, 1, MPI.OBJECT, i, MPI.ANY_TAG);
					}
					
					MPI.COMM_WORLD.Waitall(reqs);
					
					ArrayList<Group<String>> matchings = new ArrayList<Group<String>>();
					for(int i = 1; i < size; i++){
						for(Group<String> g : resps[i][0].getGroupedDNA()){
							matchings.add(g);
						}
					}
					
					reqs  = new Request[clusters];
					resps = new RetObj[clusters][1];
					
					for(int i = 0; i < clusters; i++){
						String mean = means.get(i);
						
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(mean);
						
						ReqObj message = new ReqObj(ReqType.RECALC, DataType.DNA, temp, matchings);
						
						MPI.COMM_WORLD.Isend(message, 0, 1, MPI.OJBECT, i, size);
						
						reqs[i] = MPI.COMM_WORLD.Irecv(resps[i], 0, 1, MPI.OBJECT, i, MPI.ANY_TAG);
					}
					
					MPI.COMM_WORLD.Waitall(reqs);
					
					ArrayList<String> newMeans = new ArrayList<String>();
					for(int i = 1; i < clusters; i++){
						newMeans.add(resps[i][0].getDNAMeans().get(0));
					}
					
					double maxChange = 0.0;
					for(int i = 0; i < newMeans.size(); i++) {
						double iChange = (Calcs.dnaDistance(means.get(i), newMeans.get(i)) / (dnaData.get(0).length() * 1.0));
						if (iChange > maxChange) { maxChange = iChange; }
					}
					means = newMeans;
					change = maxChange;
				}								
			} else {
				//work with points
				
				//split initial data into chunks to be processed
				ArrayList<ArrayList<Point>> chunks = new ArrayList<ArrayList<Point>>();
				for(int i = 0; i < size - 1; i++){
					chunks.add(new ArrayList<Point>());
				}
				
				for(int i = 0; i < pointData.size(); i ++){
					chunks.get(i % (size - 1)).add(pointData.get(i));
				}
				
				ArrayList<Point> means = new ArrayList<Point>();
				int partSize = pointData.size() / clusters;
				for(int i = 0; i < clusters; i++){
					Point newMean = pointData.get((int)(Math.random() * partSize) + i * partSize);
					while(means.contains(newMean)){ newMean = pointData.get((int)(Math.random() * partSize) + i * partSize); }
					means.add(newMean);
				}
				
				while(change > eps){
					Request[]  reqs  = new Request[size];
					RetObj[][] resps = new RetObj[size][1];
					
					for(int i = 1; i < size; i++){
						ReqObj message = new ReqObj(ReqType.ASSOC, DataType.POINT, means, chunks.get(i));
						
						MPI.COMM_WORLD.Isend(message, 0, 1, MPI.OBJECT, i, size);
						
						reqs[i] = MPI.COMM_WORLD.Irecv(resps[i], 0, 1, MPI.OBJECT, i, MPI.ANY_TAG);
					}
					
					MPI.COMM_WORLD.Waitall(reqs);
					
					ArrayList<Group<Point>> matchings = new ArrayList<Group<Point>>();
					for(int i = 1; i < size; i++){
						for(Group<Point> g : resps[i][0].getGroupedPoints()){
							matchings.add(g);
						}
					}
					
					reqs  = new Request[clusters];
					resps = new RetObj[clusters][1];
					
					for(int i = 0; i < clusters; i++){
						Point mean = means.get(i);
						
						ArrayList<Point> temp = new ArrayList<Point>();
						temp.add(mean);
						
						ReqObj message = new ReqObj(ReqType.RECALC, DataType.POINT, temp, matchings);
						
						MPI.COMM_WORLD.Isend(message, 0, 1, MPI.OBJECT, i, size);
						
						reqs[i] = MPI.COMM_WORLD.Irecv(resps[i], 0, 1, MPI.OBJECT, i, MPI.ANY_TAG);
					}
					
					MPI.REQUEST.Waitall(reqs);
					
					ArrayList<Point> newMeans = new ArrayList<Point>();
					for(int i = 1; i < clusters; i++){
						newMeans.add(resps[i][0].getPointMeans().get(0));
					}
					
					double maxChange = 0.0;
					for(int i = 0; i < newMeans.size(); i++) {
						double iChange = Calcs.cartesianDistance(means.get(i), newMeans.get(i));
						if (iChange > maxChange) { maxChange = iChange; }
					}
					means = newMeans;
					change = maxChange;
				}
			}
		}
	}
}
