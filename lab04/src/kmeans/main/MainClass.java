package kmeans.main;

import mpi.*;

class MainClass {
	public static void main(String[] args) throws MPIException {

		MPI.Init(args);

		int myrank = MPI.COMM_WORLD.Rank();
		int size   = MPI.COMM_WORLD.Size();
		
		if(myrank == 0){
			//I am master.
			Master.runMaster();
			
			System.out.println("This is the master, rank " + myrank);
		} else {
			//I am slave.
			Slave.runSlave();
			
			System.out.println("This is a slave, rank " + myrank);
		}
		
		System.out.println("Total size of system is: " + size);
	}
}
