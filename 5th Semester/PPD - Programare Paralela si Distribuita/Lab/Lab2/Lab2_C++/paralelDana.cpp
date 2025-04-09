#include <iostream>
#include <fstream>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <barrier>
#include <cstring>
#include <sstream>
#include <chrono>
#include <memory>  // pentru std::unique_ptr

using namespace std;

int** F;
int** C;
int n, m, k;
std::unique_ptr<std::barrier<>> barr;  // utilizăm un unique_ptr pentru gestionare sigură

void readInput(const string& filename) {
    ifstream in(filename);
    if (!in.is_open()) {
        cerr << "Error opening file: " << filename << endl;
        exit(1);
    }
    in >> n >> m;
    F = new int*[n];
    for (int i = 0; i < n; ++i) {
        F[i] = new int[m];
        for (int j = 0; j < m; ++j)
            in >> F[i][j];
    }
    in >> k;
    int temp;
    in >> temp;
    C = new int*[k];
    for (int i = 0; i < k; ++i) {
        C[i] = new int[k];
        for (int j = 0; j < k; ++j)
            in >> C[i][j];
    }
    in.close();
}

class MyThread {
    int start, end;
    int* upperBorder;
    int* lowerBorder;
    int* aux1;
    int* aux2;
public:
    MyThread(int start, int end, int m)
        : start(start), end(end) {
        upperBorder = new int[m]();
        lowerBorder = new int[m]();
        aux1 = new int[m]();
        aux2 = new int[m]();
    }
    ~MyThread() {
        delete[] upperBorder;
        delete[] lowerBorder;
        delete[] aux1;
        delete[] aux2;
    }

    void operator()() {
        int padding = k / 2;
        for (int i = start; i < end; ++i) {
            for (int j = 0; j < m; ++j) {
                int sum = 0;
                for (int i1 = 0; i1 < k; i1++) {
                    for (int j1 = 0; j1 < k; j1++) {
                        int indexI = i + i1 - padding;
                        int indexJ = j + j1 - padding;
                        if (indexI < 0) indexI = 0;
                        if (indexJ < 0) indexJ = 0;
                        if (indexI >= n) indexI = n - 1;
                        if (indexJ >= m) indexJ = m - 1;
                        sum += F[indexI][indexJ] * C[i1][j1];
                    }
                }
                if (i == start) {
                    upperBorder[j] = sum;
                } else if (i == start + 1) {
                    aux1[j] = sum;
                } else if (i == start + 2) {
                    aux2[j] = sum;
                } else if (i >= start + 3 && i < end - 1) {
                    if ((i - (start + 3)) % 2 == 0) {
                        F[i - 2][j] = aux1[j];
                        aux1[j] = sum;
                    } else {
                        F[i - 2][j] = aux2[j];
                        aux2[j] = sum;
                    }
                } else if (i == end - 1) {
                    lowerBorder[j] = sum;
                }
            }
            if (i == end - 1) {
                for (int j = 0; j < m; j++) {
                    F[i - 2][j] = aux2[j];
                    F[i - 1][j] = aux1[j];
                }
            }
        }

        barr->arrive_and_wait();
        for (int j = 0; j < m; j++) {
            F[start][j] = upperBorder[j];
            F[end - 1][j] = lowerBorder[j];
        }
    }
};

void applyParallelConvolution(int p) {
    if (p <= 0) {
        cerr << "Number of threads must be greater than zero" << endl;
        exit(1);
    }

    int startRow = 0;
    int rowsPerThread = n / p;
    int remainingRows = n % p;
    barr = std::make_unique<std::barrier<>>(p);  // inițializăm barrier cu make_unique

    vector<thread> threads;
    for (int i = 0; i < p; ++i) {
        int endRow = startRow + rowsPerThread + (remainingRows-- > 0 ? 1 : 0);
        threads.emplace_back(MyThread(startRow, endRow, m));
        startRow = endRow;
    }

    for (auto& th : threads) {
        th.join();
    }

    ofstream out("D:/Ariana/Facultate/SEM5/PPD/LAB/Lab2/Lab2_C++/output.txt");
    if (!out.is_open()) {
        cerr << "Error opening output file" << endl;
        exit(1);
    }
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < m; ++j) {
            out << F[i][j] << " ";
        }
        out << endl;
    }
    out.close();
}

int main() {
    readInput("D:/Ariana/Facultate/SEM5/PPD/LAB/Lab2/Lab2_C++/dana.txt");
    int p = 2;

    auto start = chrono::high_resolution_clock::now();
    applyParallelConvolution(p);
    auto end = chrono::high_resolution_clock::now();
    cout << "Execution time: " << chrono::duration_cast<chrono::milliseconds>(end - start).count() << " ms" << endl;



    for (int i = 0; i < n; ++i) {
        delete[] F[i];
    }
    delete[] F;
    for (int i = 0; i < k; ++i) {
        delete[] C[i];
    }
    delete[] C;

    return 0;
}
