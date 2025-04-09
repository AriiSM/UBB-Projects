//#include <iostream>
//#include <vector>
//#include <fstream>
//#include <algorithm>
//#include <mpi.h>
//#include <cstdint>
//#include <string>
//using namespace std;
//
//bool compareFiles(const char* file1, const char* file2) {
//    ifstream f1(file1);
//    ifstream f2(file2);
//
//    if (!f1.is_open() || !f2.is_open()) {
//        cerr << "Eroare la deschiderea fișierelor pentru comparație!" << endl;
//        return false;
//    }
//
//    string line1, line2;
//    while (getline(f1, line1) && getline(f2, line2)) {
//        if (line1 != line2) {
//            return false;
//        }
//    }
//
//    return f1.eof() && f2.eof();
//}
//
//int main() {
//
//    if (compareFiles("D:/Ariana/Facultate/SEM5/PPD/LAB/Lab3/Lab3_MPI/MPI/Rezultat.txt", "D:/Ariana/Facultate/SEM5/PPD/LAB/Lab3/Lab3_MPI/MPI/Secvential.txt")) {
//        std::cout << "Fișierele sunt identice." << std::endl;
//    }
//    else {
//        std::cout << "Fișierele sunt diferite." << std::endl;
//    }
//
//    return 0;
//}
