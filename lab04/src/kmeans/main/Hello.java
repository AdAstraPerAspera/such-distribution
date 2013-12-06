package kmeans.main;

import mpi.*;
 
class Hello {
    static public void main(String[] args) throws MPIException {
      

        MPI.Init(args);

        int myrank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        if(myrank == 0){
        	String message = "such message".toCharArray();
        	MPI.COMM_WORLD.Send(message, 0, message.length, MPI.CHAR, 1, size);
        } else {
        	char[] message = new char[12];
        	MPI.COMM_WORLD.Recv(message, 0, 20, MPI.CHAR, 0, 99);
        	
        	System.out.println("received message: " + new String(message) + " :");
        }
        System.out.println("Hello world from rank " + myrank + " of " + size);
 
        MPI.Finalize();
    }
}
