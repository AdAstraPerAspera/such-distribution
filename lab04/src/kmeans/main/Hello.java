package kmeans.main;

import mpi.*;
 
class Hello {
    static public void main(String[] args) throws MPIException {
      

        MPI.Init(args);

        int myrank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size() ;
        System.out.println("Hello world from rank " + myrank + " of " + size);
 
        MPI.Finalize();
    }
}
