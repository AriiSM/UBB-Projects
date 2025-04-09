//#include "mpi.h"
//#include <stdio.h>
//#include <stdlib.h>
//#include <random>
//#include <chrono>
//#include <iostream>
//#include <vector>
//#include <chrono>
//using namespace std;
//
//#include "management.h"
//
//// Functia de adunare a numerelor mari
//pair<vector<int>, int> addLargeNumbers(const vector<int>& vec1, const vector<int>& vec2, int initialCarry) {
//    int size = vec1.size();
//    vector<int> result(size), vec11, vec22;
//    int carry = initialCarry;
//
//    //vec11 = reverseVector(vec1);
//    //vec22 = reverseVector(vec2);
//
//    for (int i = 0; i < size; i++) {
//        int sum = vec1[i] + vec2[i] + carry;
//        result[i] = sum % 10;
//        carry = sum / 10;
//    }
//
//    //result = reverseVector(result);
//    return { result, carry };  // returnează carry-ul final pentru următorul proces
//}
//
//int main(int argc, char** argv) {
//    int rank, numprocs;
//    MPI_Init(&argc, &argv);
//    MPI_Comm_size(MPI_COMM_WORLD, &numprocs);
//    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
//    MPI_Status status;
//
//    vector<int> vector1, vector2, rezultat;
//    int chunck_size, rest;
//    int finalCarry = 0;
//
//    auto startTime = std::chrono::high_resolution_clock::now();
//
//    if (rank == 0) {
//        vector1 = readVectorFromFile("D:/Ariana/Facultate/SEM5/PPD/LAB/Lab3/Lab3_MPI/MPI/Numar1.txt");
//        vector2 = readVectorFromFile("D:/Ariana/Facultate/SEM5/PPD/LAB/Lab3/Lab3_MPI/MPI/Numar2.txt");
//
//        int maxSize = max(vector1.size(), vector2.size());
//
//        // Ajustăm maxSize astfel încât să fie divizibil cu numărul de procese
//        if (maxSize % numprocs != 0) {
//            maxSize += numprocs - (maxSize % numprocs);
//        }
//
//        vector1.resize(maxSize, 0);
//        vector2.resize(maxSize, 0);
//        rezultat.resize(maxSize, 0);
//    }
//
//    int SIZE;
//    if (rank == 0) {
//        SIZE = vector1.size();
//    }
//
//    MPI_Bcast(&SIZE, 1, MPI_INT, 0, MPI_COMM_WORLD);
//
//    chunck_size = SIZE / numprocs;
//    rest = SIZE % numprocs;
//
//    // Fiecare proces va primi un chunk de date
//    int local_size = chunck_size + (rank < rest ? 1 : 0);
//    vector<int> auxVector1(local_size), auxVector2(local_size), auxRezultat(local_size);
//    int localCarry = 0;
//
//    // Distribuirea vectorilor între procese
//    MPI_Scatter(vector1.data(), local_size, MPI_INT, auxVector1.data(), local_size, MPI_INT, 0, MPI_COMM_WORLD);
//    MPI_Scatter(vector2.data(), local_size, MPI_INT, auxVector2.data(), local_size, MPI_INT, 0, MPI_COMM_WORLD);
//
//    // Fiecare proces calculează suma segmentului
//    auto resultCarry = addLargeNumbers(auxVector1, auxVector2, localCarry);
//    auxRezultat = resultCarry.first;
//    localCarry = resultCarry.second;
//
//
//    // Primește carry-ul de la procesul anterior
//    if (rank > 0) {
//        int previousCarry;
//        MPI_Recv(&previousCarry, 1, MPI_INT, rank - 1, 0, MPI_COMM_WORLD, &status);
//
//        // Recalculează ultima cifră cu noul carry
//        auto lastResult = addLargeNumbers(auxRezultat, vector<int>(local_size, 0), previousCarry);
//        auxRezultat = lastResult.first;
//        localCarry = lastResult.second;
//
//        // Dacă este ultimul proces, trimite carry-ul final procesului 0
//        if (rank == 1) {
//            MPI_Send(&localCarry, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
//        }
//    }
//
//    // Trimite carry-ul la următorul proces
//    if (rank < numprocs - 1) {
//        MPI_Send(&localCarry, 1, MPI_INT, rank + 1, 0, MPI_COMM_WORLD);
//    }
//
//    // Colectează rezultatul final la procesul 0
//    MPI_Gather(auxRezultat.data(), local_size, MPI_INT, rezultat.data(), local_size, MPI_INT, 0, MPI_COMM_WORLD);
//
//    // Procesul 0 gestionează carry-ul final
//    if (rank == 0) {
//        // Așteaptă și colectează carry-ul final
//        int finalCarryFromLastProcess;
//        MPI_Recv(&finalCarryFromLastProcess, 1, MPI_INT, 1, 0, MPI_COMM_WORLD, &status);
//
//        // Adaugă carry-ul final la rezultat dacă este nenul
//        if (finalCarryFromLastProcess == 1) {
//            rezultat.push_back(1);
//        }
// 
//
//        auto endTime = std::chrono::high_resolution_clock::now();
//        auto duration = std::chrono::duration_cast<std::chrono::nanoseconds>(endTime - startTime).count();
//        double durationInSeconds = static_cast<double>(duration) / 1e9; // Divide by 1e9 to convert nanoseconds to seconds
//        cout << "Durata: " << durationInSeconds << " s" << endl;
//
//
//        cout << "Numar 1: ";
//        printVector(vector1);
//        cout << "Numar 2: ";
//        printVector(vector2);
//        // Afișează rezultatul final
//        cout << "Suma:    ";
//		rezultat = rezultat;
//        printVector(reverseVector(rezultat));
//        saveVectorToFile(reverseVector(rezultat), "D:/Ariana/Facultate/SEM5/PPD/LAB/Lab3/Lab3_MPI/MPI/Rezultat.txt");
//    }
//
//    MPI_Finalize();
//    return 0;
//}
