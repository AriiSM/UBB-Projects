//#include "mpi.h"
//#include <stdio.h>
//#include <stdlib.h>
//#include <random>
//#include <iostream>
//#include <vector> 
//#include <chrono>
//using namespace std;
//#include "management.h";
//
//vector<int> addLargeNumbers(const vector<int>& vec1, const vector<int>& vec2) {
//    int size = vec1.size();
//    vector<int> result(size), vec11, vec22;
//    int carry = 0;
//
//    vec11 = reverseVector(vec1);
//    vec22 = reverseVector(vec2);
//
//    for (int i = 0; i < size; i++) {
//        int sum = vec11[i] + vec22[i] + carry;
//        result[i] = sum % 10;
//        carry = sum / 10;
//    }
//
//    if (carry > 0) {
//        result.push_back(carry);
//    }
//
//    result = reverseVector(result);
//    return result;
//}
//
//int main(int argc, char** argv) {
//    vector<int> vector1, vector2, reversedVector1, reversedVector2;
//    int size1, size2;
//
//    // Generare si salvare vectori random in fișiere
//    //createvectors();
//
//    auto startTime = std::chrono::high_resolution_clock::now();
//
//    // Citirea vectorului din fisierul Numar1.txt
//    vector1 = readVectorFromFile("D:/Ariana/Facultate/SEM5/PPD/LAB/Lab3/Lab3_MPI/MPI/Numar1.txt");
//    // Citirea vectorului din fisierul Numar2.txt
//    vector2 = readVectorFromFile("D:/Ariana/Facultate/SEM5/PPD/LAB/Lab3/Lab3_MPI/MPI/Numar2.txt");
//
//    printVector(vector1);
//    printVector(vector2);
//
//    vector<int> sumResult = addLargeNumbers(vector1, vector2);
//
//    auto endTime = std::chrono::high_resolution_clock::now();
//    auto duration = std::chrono::duration_cast<std::chrono::nanoseconds>(endTime - startTime).count();
//    double durationInSeconds = static_cast<double>(duration) / 1e9; // Divide by 1e9 to convert nanoseconds to seconds
//    cout << "Durata: " << durationInSeconds << " s" << endl;
//
//    printVector(sumResult);
//
//    saveVectorToFile(sumResult, "D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab3\\Lab3_MPI\\MPI\\Secvential.txt");
//}