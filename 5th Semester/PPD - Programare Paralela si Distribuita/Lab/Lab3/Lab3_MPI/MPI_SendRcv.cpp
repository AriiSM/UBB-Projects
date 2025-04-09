#include "mpi.h"
#include <stdio.h>
#include <stdlib.h>
#include <random>
#include <iostream>
#include <chrono>
#include <vector>
using namespace std;

#include "management.h"

const int SIZE = 16;  // Dimensiunea vectorilor

pair<vector<int>, int> addLargeNumbers(const vector<int>& vec1, const vector<int>& vec2, int initialCarry) {
    int size = vec1.size();
    vector<int> result(size), vec11, vec22;
    int carry = initialCarry;

    //vec11 = reverseVector(vec1);
    //vec22 = reverseVector(vec2);

    for (int i = 0; i < size; i++) {
        int sum = vec1[i] + vec2[i] + carry;
        result[i] = sum % 10;
        carry = sum / 10;
    }

    //result = reverseVector(result);
    return { result, carry };  // returnează carry-ul final pentru următorul proces
}

int main(int argc, char** argv)
{
    //createvectors();

    int rank, numprocs;
    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &numprocs);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Status nr1Status, nr2Status, carryStatus, startStatus, endStatus, lungimeStatus, rezStatus;

    vector<int> vector1(SIZE), vector2(SIZE), rezultat(SIZE);
    int start, end;

    auto startTime = std::chrono::high_resolution_clock::now();

    if (rank == 0) {
        // Calculăm pasul și restul pentru împărțirea datelor
        int step = SIZE / (numprocs - 1);
        int rest = SIZE % (numprocs - 1);
        start = 0;

        // Etapa 2a: Procesul 0 citește și trimite segmentele
        for (int i = 1; i < numprocs; i++) {
            end = start + step - 1;
            if (rest > 0) {
                end += 1;
                rest -= 1;
            }

            // Citește și trimite segmentele din fișiere
            vector1 = readVectorFromFileManipulated("D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab3\\Lab3_MPI\\MPI\\Numar1.txt", start, end);
            vector2 = readVectorFromFileManipulated("D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab3\\Lab3_MPI\\MPI\\Numar2.txt", start, end);


            MPI_Send(&start, 1, MPI_INT, i, 0, MPI_COMM_WORLD);
            MPI_Send(&end, 1, MPI_INT, i, 0, MPI_COMM_WORLD);

            int lungime = vector1.size();
            MPI_Send(&lungime, 1, MPI_INT, i, 0, MPI_COMM_WORLD);

            MPI_Send(vector1.data(), vector1.size(), MPI_INT, i, 0, MPI_COMM_WORLD);
            MPI_Send(vector2.data(), vector2.size(), MPI_INT, i, 0, MPI_COMM_WORLD);

            start = end + 1;
        }

        // Etapa 5: Procesul 0 primește rezultatele de la toate procesele
        int carry = 0;
        for (int i = 1; i < numprocs; i++) {

            int carryFromProcess;
            MPI_Recv(&start, 1, MPI_INT, i, 0, MPI_COMM_WORLD, &startStatus);
            MPI_Recv(&end, 1, MPI_INT, i, 0, MPI_COMM_WORLD, &endStatus);
            MPI_Recv(&carryFromProcess, 1, MPI_INT, i, 0, MPI_COMM_WORLD, &carryStatus);
            MPI_Recv(rezultat.data() + start, end - start + 1, MPI_INT, i, 0, MPI_COMM_WORLD, &rezStatus);

            carry = carryFromProcess;
            std::cout << "Procesul 0 a primit de la " << i << endl;
			printVector(rezultat);

        }

        // Dacă există carry rămas, se adaugă la rezultatul final
        if (carry > 0) {
            rezultat = rezultat;
            rezultat.push_back(carry);
            rezultat = rezultat;
        }

        auto endTime = std::chrono::high_resolution_clock::now();
        auto duration = std::chrono::duration_cast<std::chrono::nanoseconds>(endTime - startTime).count();
        double durationInSeconds = static_cast<double>(duration) / 1e9; // Divide by 1e9 to convert nanoseconds to seconds
        cout << "Durata: " << durationInSeconds << " s" << endl;

        cout << "Suma corecta: ";
        printVector(reverseVector(rezultat));
        saveVectorToFile(reverseVector(rezultat), "D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab3\\Lab3_MPI\\MPI\\Rezultat.txt");

    }
    else {
        // Etapa 3: Procesele primesc datele necesare de la procesul 0
        MPI_Recv(&start, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &startStatus);
        MPI_Recv(&end, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &endStatus);

        int lungime;
        MPI_Recv(&lungime, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &lungimeStatus);

        vector<int> nr1(lungime);
        vector<int> nr2(lungime);

        MPI_Recv(nr1.data(), lungime, MPI_INT, 0, 0, MPI_COMM_WORLD, &nr1Status);
        MPI_Recv(nr2.data(), lungime, MPI_INT, 0, 0, MPI_COMM_WORLD, &nr2Status);

        cout << "Procesul " << rank << " a primit de la 0 " << " start " << start << " cu end " << end << endl;
        printVector(nr1);
        printVector(nr2);

        // Etapa 3: Primește carry-ul de la procesul anterior
        int initialCarry;
        if (rank == 1) {
            initialCarry = 0; // Procesul 1 nu primește carry, consideră 0
        }
        else {
            MPI_Recv(&initialCarry, 1, MPI_INT, rank - 1, 0, MPI_COMM_WORLD, &carryStatus);
        }

        // Etapa 4: Adunarea numerelor mari și propagarea carry
        pair<vector<int>, int> additionResult = addLargeNumbers(nr1, nr2, initialCarry);
        vector<int> result = additionResult.first;
        int localCarry = additionResult.second;
        cout << "Proces " << rank << "    ";
        printVector(result);

        // Trimite carry-ul către următorul proces
        if (rank != numprocs - 1) {
            MPI_Send(&localCarry, 1, MPI_INT, rank + 1, 0, MPI_COMM_WORLD);
        }

        MPI_Send(&start, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
        MPI_Send(&end, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
        MPI_Send(&localCarry, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
        MPI_Send(result.data(), result.size(), MPI_INT, 0, 0, MPI_COMM_WORLD);
    }

    MPI_Finalize();
    return 0;
}