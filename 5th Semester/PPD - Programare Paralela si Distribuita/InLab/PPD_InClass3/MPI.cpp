#include "mpi.h"
#include <stdio.h>
#include <stdlib.h>
#include <random>
#include<iostream>
using namespace std;

const int n = 10;


void print(int* a, int n) {
    for (int i = 0; i < n; ++i) {
        cout << a[i] << ' ';
    }
    cout << endl;
} 

//int main(int argc, char** argv)
//{
//    //HELLO WORLD CURS:
//   /* 
//   int namelen, myid, numprocs;
//    MPI_Init(&argc, &argv);
//    MPI_Comm_size(MPI_COMM_WORLD, &numprocs);
//    MPI_Comm_rank(MPI_COMM_WORLD, &myid);
//    printf("Process %d / %d : Hello world\n", myid, numprocs);
//
//    MPI_Finalize();
//    return 0;
//    */
//
//    int rank, numproc;
//    int sum = 0;
//    int total_sum = 0;
//
//    MPI_Init(&argc, &argv);
//    MPI_Comm_size(MPI_COMM_WORLD, &numproc);
//    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
//
//	int a[n], b[n], c[n];
//    int start, end;
//	MPI_Status status;
//
//
//    if (rank == 0) {
//        //master code
//        for (int i = 0; i < n; i++) {
//			a[i] = rand() % 10;
//			b[i] = rand() % 10;
//        }
//
//        int p = numproc - 1;
//		int step = n / p;
//		int rest = n % p;
//
//        int start = 0;
//		int end = step;
//
//        for (int i = 1; i < numproc; i++) {
//            if (rest > 0) {
//                end++;
//                rest--;
//            }
//
//			MPI_Send(&start, 1, MPI_INT, i, 0, MPI_COMM_WORLD);
//			MPI_Send(&end, 1, MPI_INT, i, 0, MPI_COMM_WORLD);
//            MPI_Send(a + start, end - start, MPI_INT, i, 0, MPI_COMM_WORLD);
//            MPI_Send(b + start, end - start, MPI_INT, i, 0, MPI_COMM_WORLD);
//
//
//            start = end;
//            end = start + step;
//        }
//
//
//        for (int i = 1; i < numproc; i++) {
//			MPI_Recv(&start, 1, MPI_INT, i, 0, MPI_COMM_WORLD, &status);
//			MPI_Recv(&end, 1, MPI_INT, i, 0, MPI_COMM_WORLD, &status);
//			MPI_Recv(c + start, end - start, MPI_INT, i, 0, MPI_COMM_WORLD, &status);
//        }
//
//        print(a, n);
//        print(b, n);
//        print(c, n);
//
//
//    }
//    else {
//        //slave code
//		MPI_Recv(&start, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
//        MPI_Recv(&end, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
//		MPI_Recv(a + start, end - start, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
//		MPI_Recv(b + start, end - start, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
//    
//        for (int i = start; i < end; i++) {
//            c[i] = a[i] + b[i];
//        }
//
//        MPI_Send(&start, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
//        MPI_Send(&end, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
//        MPI_Send(c + start, end - start, MPI_INT, 0, 0, MPI_COMM_WORLD);
//    
//       
//    }
//    MPI_Finalize();
//    return 0;
//}


int main(int argc, char** argv)
{
    int rank, numproc;
    int sum = 0;
    int total_sum = 0;

    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &numproc);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);

    int a[n], b[n], c[n];
    MPI_Status status;


    if (rank == 0) {
        //master code
        for (int i = 0; i < n; i++) {
            a[i] = rand() % 10;
            b[i] = rand() % 10;
        }


    }

    int chunck_size = n / numproc;

    int* auxA = new int[chunck_size];
    int* auxB = new int[chunck_size];
    int* auxC = new int[chunck_size];

    MPI_Scatter(a, chunck_size, MPI_INT, auxA, chunck_size, MPI_INT, 0, MPI_COMM_WORLD);
    MPI_Scatter(b, chunck_size, MPI_INT, auxB, chunck_size, MPI_INT, 0, MPI_COMM_WORLD);

    for (int i = 0; i < chunck_size; i++) {
        auxC[i] = auxA[i] + auxB[i];
    }

    MPI_Gather(auxC, chunck_size, MPI_INT, c, chunck_size, MPI_INT, 0, MPI_COMM_WORLD);

    if (rank == 0) {
		print(a, n);
		print(b, n);
		print(c, n);
    }

    MPI_Finalize();
    return 0;
}