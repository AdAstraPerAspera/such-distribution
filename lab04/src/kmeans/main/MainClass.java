package kmeans.main;

import mpi.*;

class MainClass {
	//TODO: change back to private after migrating Master/Slave funcs back
	public static void printHelp(){
	    System.out.println("Need arguments to run:");
    	System.out.println("If using DNA: use arguments \"-d <length of strings> <input file path> <# of clusters> <epsilon>\"");
    	System.out.println("If using points: use arguments \"-p <input file path> <# of clusters> <epsilon>\"");
    	System.exit(0);
  	}
	
	public static void main(String[] args) throws MPIException {
		args = MPI.Init(args);

		int myrank = MPI.COMM_WORLD.Rank();
		int size   = MPI.COMM_WORLD.Size();
		
		if(myrank == 0){
			//I am master.
			Master.runMaster(args, size);
			
			System.out.println("This is the master, rank " + myrank);
		} else {
			//I am slave.
			Slave.runSlave();
			
			System.out.println("This is a slave, rank " + myrank);
		}
		
		System.out.println("Total size of system is: " + size);
		
		MPI.Finalize();
	}
}
