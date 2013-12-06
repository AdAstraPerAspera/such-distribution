package kmeans.main;

import mpi.*;
 
class Hello {
    static public void main(String[] args) throws MPIException {
      

        MPI.Init(args);

        int myrank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        if(myrank == 0){
        	char[] message = "such message".toCharArray();
        	for(int i = 1; i < size; i++){
        		MPI.COMM_WORLD.Send(message, 0, message.length, MPI.CHAR, i, size);
        		
        		System.out.println("Sending message to node: " + (i + 1));
        	}
        } else {
        	char[] message = new char[12];
        	MPI.COMM_WORLD.Recv(message, 0, 12, MPI.CHAR, 0, MPI.ANY_TAG);
        	
        	System.out.println("received message: " + new String(message) + " :");
        }
        System.out.println("Hello world from rank " + myrank + " of " + size);
 
        MPI.Finalize();
    }
}
