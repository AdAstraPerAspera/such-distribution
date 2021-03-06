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
	public static void termAll(int size) throws Exception{
		ReqObj termReq = new ReqObj();
		ReqObj[] buf = new ReqObj[1];
		buf[0] = termReq;
		
		for(int i = 1; i < size; i++){
			MPI.COMM_WORLD.Send(buf, 0, 1, MPI.OBJECT, i, size);
		}
	}
	
	public static void runMaster(String[] args, int size) throws Exception{
		//grab data from command line arguments
		DataType type      			  = null;
		int 				length	  = 0;
		int                 clusters  = 0;
		double              eps       = 0.0;
		double              change    = Double.MAX_VALUE;
		ArrayList<String>   dnaData   = new ArrayList<String>();
		ArrayList<Point>    pointData = new ArrayList<Point>();
		
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
			
			while((line = reader.readLine()) != null && line.length() == length){
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
		
		//TODO: send messages and shit
		if(type == DataType.DNA){
			//work with strings
			
			//split initial data into chunks to be processed
			ArrayList<ArrayList<String>> chunks = new ArrayList<ArrayList<String>>();
			for(int i = 0; i < size - 1; i++){
				chunks.add(new ArrayList<String>());
			}
			
			for(int i = 0; i < dnaData.size(); i++){
					chunks.get(i % (size - 1)).add(dnaData.get(i));
			}
			
			ArrayList<String> means = new ArrayList<String>();
			int partSize = dnaData.size() / clusters;
			for(int i = 0; i < clusters; i++){
				String newMean = dnaData.get((int)(Math.random() * partSize) + i * partSize);
				while(means.contains(newMean)) newMean = dnaData.get((int)(Math.random() * partSize) + i * partSize);
				means.add(newMean);
			}
			
			while(change > eps){
				Object[][] resps = new Object[size][1];
				
				for(int i = 1; i < size; i++){
					ReqObj message = new ReqObj(ReqType.ASSOC, DataType.DNA, means, chunks.get(i - 1));
					Object[] buf = new Object[1];
					buf[0] = message;
					
					MPI.COMM_WORLD.Isend(buf, 0, 1, MPI.OBJECT, i, size);
				}
				
				for(int i = 1; i < size; i++){
					MPI.COMM_WORLD.Recv(resps[i], 0, 1, MPI.OBJECT, i, MPI.ANY_TAG);
				}
				
				ArrayList<Group<String>> matchings = new ArrayList<Group<String>>();
				for(int i = 1; i < size; i++){
					for(Group<String> g : ((RetObj) resps[i][0]).getGroupedDNA()){
						matchings.add(g);
					}
				}
				
				resps = new Object[clusters][1];
				
				for(int i = 0; i < clusters; i++){
					String mean = means.get(i);
					
					ArrayList<String> temp = new ArrayList<String>();
					temp.add(mean);
					
					ReqObj message = new ReqObj(ReqType.RECALC, DataType.DNA, temp, matchings);
					Object[] buf = new Object[1];
					buf[0] = message;
					
					MPI.COMM_WORLD.Isend(buf, 0, 1, MPI.OBJECT, i % (size - 1) + 1, size);
				}
				
				for(int i = 0; i < clusters; i++){
					MPI.COMM_WORLD.Recv(resps[i], 0, 1, MPI.OBJECT, i % (size - 1) + 1, MPI.ANY_TAG);
				}
				
				ArrayList<String> newMeans = new ArrayList<String>();
				for(int i = 0; i < clusters; i++){
					newMeans.add(((RetObj) resps[i][0]).getDNAMeans().get(0));
				}
				
				double maxChange = 0.0;
				for(int i = 0; i < newMeans.size(); i++) {
					double iChange = (Calcs.dnaDistance(means.get(i), newMeans.get(i)) / (length * 1.0));
					if (iChange > maxChange) { maxChange = iChange; }
				}
				means = newMeans;
				change = maxChange;
			}
			for(String s : means){
				System.out.println(s);
			}
			
			termAll(size);
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
				Object[][] resps = new Object[size][1];

				for(int i = 1; i < size; i++){
					ReqObj message = new ReqObj(ReqType.ASSOC, DataType.POINT, means, chunks.get(i - 1));
					ReqObj[] buf = new ReqObj[1];
					buf[0] = message;
					
					MPI.COMM_WORLD.Isend(buf, 0, 1, MPI.OBJECT, i, size);
				}

				for(int i = 1; i < size; i++){
					MPI.COMM_WORLD.Recv(resps[i], 0, 1, MPI.OBJECT, i, MPI.ANY_TAG);
				}

				ArrayList<Group<Point>> matchings = new ArrayList<Group<Point>>();
				for(int i = 1; i < size; i++){
					for(Group<Point> g : ((RetObj) resps[i][0]).getGroupedPoints()){
						matchings.add(g);
					}
				}
				
				resps = new Object[clusters][1];

				for(int i = 0; i < clusters; i++){
					Point mean = means.get(i);
					
					ArrayList<Point> temp = new ArrayList<Point>();
					temp.add(mean);
					
					ReqObj message = new ReqObj(ReqType.RECALC, DataType.POINT, temp, matchings);
					Object[] buf = new Object[1];
					buf[0] = message;
					
					MPI.COMM_WORLD.Isend(buf, 0, 1, MPI.OBJECT, i % (size - 1) + 1, size);
				}
				
				for(int i = 0; i < clusters; i++){
					MPI.COMM_WORLD.Recv(resps[i], 0, 1, MPI.OBJECT, i % (size - 1) + 1, MPI.ANY_TAG);
				}
				
				ArrayList<Point> newMeans = new ArrayList<Point>();
				for(int i = 0; i < clusters; i++){
					newMeans.add(((RetObj) resps[i][0]).getPointMeans().get(0));
				}
				
				double maxChange = 0.0;
				for(int i = 0; i < newMeans.size(); i++) {
					double iChange = Calcs.cartesianDistance(means.get(i), newMeans.get(i));
					if (iChange > maxChange) { maxChange = iChange; }
				}
				means = newMeans;
				change = maxChange;
			}
			
			for(Point p : means){
				System.out.println(p.getX() + ", " + p.getY());
			}
			
			termAll(size);
		}
	}
}
