#include <iostream>
#include <thread>
#include <vector>
#include <chrono>
#include <fstream>

#define N 10  // Number of rows in the matrix
#define M 10  // Number of columns in the matrix
#define K 3     // Filter size
#define P 2    // Number of threads

int F[N][M]; // Input matrix
int C[K][K]; // Convolution filter
int V[N][M]; // Output matrix
int F_padded[N + 2][M + 2] = {0}; // Padded input matrix

void readMatricesFromFile(const std::string &filename) {
    std::ifstream file(filename);

    if (!file) {
        std::cerr << "Eroare la deschiderea fisierului!" << std::endl;
        exit(1);
    }

    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < M; ++j) {
            file >> F[i][j];
        }
    }

    for (int i = 0; i < K; ++i) {
        for (int j = 0; j < K; ++j) {
            file >> C[i][j];
        }
    }
}

void writeMatrixToFile(const std::string &filename) {
    std::ofstream file(filename);

    if (!file) {
        std::cerr << "Eroare la deschiderea fisierului!" << std::endl;
        exit(1);
    }

    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < M; ++j) {
            file << V[i][j] << " ";
        }
        file << std::endl;
    }
}

void worker(int startCol, int endCol) {
    int padSize = K / 2;

    for (int j = startCol; j < endCol; j++) {
        for (int i = 0; i < N; ++i) {
            int sum = 0;
            for (int ki = 0; ki < K; ki++) {
                for (int kj = 0; kj < K; kj++) {
                    sum += F_padded[i + ki][j + kj] * C[ki][kj];
                }
            }
            V[i][j] = sum;
        }
    }
}

int main() {
    readMatricesFromFile(
        "D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab1\\Lab1_JAVA\\src\\main\\java\\org\\example\\date\\date.txt");

    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < M; ++j) {
            F_padded[i + 1][j + 1] = F[i][j];
        }
    }


    auto start = std::chrono::high_resolution_clock::now();

    std::vector<std::thread> threads;

    int chunkSize = M / P;

    for (int t = 0; t < P; ++t) {
        int startCol = t * chunkSize;
        int endCol = (t == P - 1) ? M : startCol + chunkSize;

        threads.push_back(std::thread(worker, startCol, endCol));
    }

    for (auto &th: threads) {
        th.join();
    }

    auto end = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double, std::milli> duration = end - start;


    std::cout << "Durata: " << duration << std::endl;

    writeMatrixToFile(
        "D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab1\\Lab1_JAVA\\src\\main\\java\\org\\example\\date\\output.txt");

    return 0;
}
