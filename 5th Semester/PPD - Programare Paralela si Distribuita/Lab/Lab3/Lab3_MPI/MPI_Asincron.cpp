//#include "mpi.h"
//#include <iostream>
//#include <vector>
//#include <fstream>
//#include <algorithm> // pentru reverse
//#include "management.h"
//using namespace std;
//
//// Funcție pentru adunarea numerelor mari reprezentate ca vectori de cifre
//pair<vector<int>, int> addLargeNumbers(const vector<int>& vec1, const vector<int>& vec2, int initialCarry) {
//    int size = vec1.size();
//    vector<int> result(size);
//    int carry = initialCarry;
//
//    for (int i = 0; i < size; i++) {
//        int sum = vec1[i] + vec2[i] + carry;
//        result[i] = sum % 10;
//        carry = sum / 10;
//    }
//    return { result, carry };  // returnează carry-ul final pentru următorul proces
//}
//
//int main(int argc, char** argv)
//{
//    int rank, numprocs;
//    MPI_Init(&argc, &argv);
//    MPI_Comm_size(MPI_COMM_WORLD, &numprocs);
//    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
//
//    vector<int> vector1, vector2, rezultat;
//    int SIZE = 0;
//    int chunck_size = 5;  // Exemplu de dimensiune vector, poate fi adaptat
//    int rest = SIZE % numprocs;  // restul care trebuie distribuit
//
//    // Procesul 0 citește datele
//    if (rank == 0) {
//        vector1 = readVectorFromFile("D:/Ariana/Facultate/SEM5/PPD/LAB/Lab3/Lab3_MPI/MPI/Numar1.txt");
//        vector2 = readVectorFromFile("D:/Ariana/Facultate/SEM5/PPD/LAB/Lab3/Lab3_MPI/MPI/Numar2.txt");
//
//        if (vector1.size() > vector2.size()) {
//            vector2.resize(vector1.size(), 0);
//        }
//        else {
//            vector1.resize(vector2.size(), 0);
//        }
//
//        // Inversează vectorii pentru a aduna de la cel mai puțin semnificativ
//        vector1 = reverseVector(vector1);
//        vector2 = reverseVector(vector2);
//        SIZE = vector1.size();
//    }
//
//    // Fiecare proces va primi un chunk de date (cu restul distribuit la primele procese)
//    int local_size = chunck_size + (rank < rest ? 1 : 0);  // procesele care primesc restul primesc un element suplimentar
//    vector<int> auxVector1(local_size), auxVector2(local_size), auxRezultat(local_size);
//    int localCarry = 0;
//
//    // Vom folosi MPI_Isend și MPI_Irecv pentru a trimite și a primi date asincron
//    vector<MPI_Request> send_requests(numprocs - 1);  // array pentru cereri de trimitere
//    vector<MPI_Request> recv_requests(numprocs - 1);  // array pentru cereri de recepție
//
//    if (rank == 0) {
//        // Trimite datele asincron
//        for (int i = 1; i < numprocs; i++) {
//            int local_size_for_process = chunck_size + (i < rest ? 1 : 0);
//            MPI_Isend(&vector1[i * chunck_size], local_size_for_process, MPI_INT, i, 1, MPI_COMM_WORLD, &send_requests[i - 1]);
//            MPI_Isend(&vector2[i * chunck_size], local_size_for_process, MPI_INT, i, 2, MPI_COMM_WORLD, &send_requests[i + numprocs - 2]);
//        }
//    }
//
//    // Procesele slave primesc datele asincron
//    MPI_Irecv(auxVector1.data(), local_size, MPI_INT, 0, 1, MPI_COMM_WORLD, &recv_requests[rank - 1]);
//    MPI_Irecv(auxVector2.data(), local_size, MPI_INT, 0, 2, MPI_COMM_WORLD, &recv_requests[rank - 1]);
//
//    // Așteaptă finalizarea recepției
//    MPI_Waitall(numprocs - 1, recv_requests.data(), MPI_STATUSES_IGNORE);
//
//    // Adună local segmentele, cu carry inițial 0
//    auto resultCarry = addLargeNumbers(auxVector1, auxVector2, localCarry);
//    auxRezultat = resultCarry.first;
//    localCarry = resultCarry.second;
//
//    // Trimite rezultatele adunării înapoi la procesul 0 asincron
//    vector<MPI_Request> gather_requests(numprocs);
//    MPI_Isend(auxRezultat.data(), local_size, MPI_INT, 0, 3, MPI_COMM_WORLD, &gather_requests[rank]);
//
//    // Procesul 0 colectează rezultatele adunării de la toate procesele asincron
//    if (rank == 0) {
//        vector<int> carries(numprocs);
//        for (int i = 1; i < numprocs; i++) {
//            MPI_Irecv(rezultat.data(), local_size, MPI_INT, i, 3, MPI_COMM_WORLD, &gather_requests[i - 1]);
//        }
//
//        // Colectează carry-urile de la toate procesele
//        MPI_Gather(&localCarry, 1, MPI_INT, carries.data(), 1, MPI_INT, 0, MPI_COMM_WORLD);
//
//        // La procesul 0, adunăm rezultatele și gestionăm carry-ul final
//        int finalCarry = 0;
//        for (int i = 0; i < numprocs; i++) {
//            finalCarry += carries[i];
//        }
//
//        if (finalCarry > 0) {
//            rezultat.push_back(finalCarry);
//        }
//
//        // Afișează rezultatele finale
//        cout << "Numar 1: ";
//        for (int i = vector1.size() - 1; i >= 0; i--) {
//            cout << vector1[i] << " ";
//        }
//        cout << endl;
//
//        cout << "Numar 2: ";
//        for (int i = vector2.size() - 1; i >= 0; i--) {
//            cout << vector2[i] << " ";
//        }
//        cout << endl;
//
//        cout << "Suma corecta: ";
//        for (int i = rezultat.size() - 1; i >= 0; i--) {
//            cout << rezultat[i] << " ";
//        }
//        cout << endl;
//    }
//
//    MPI_Finalize();
//    return 0;
//}
