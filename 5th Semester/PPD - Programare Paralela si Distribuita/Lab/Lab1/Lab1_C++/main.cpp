#include <iostream>
#include <fstream>
#include <vector>
#include <chrono>
#include <stdexcept>
#include "WriterAndReader.h"
#include "Dinamic/Convolutie.h"

std::string filePathRead = "D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab1\\Lab1_JAVA\\src\\main\\java\\org\\example\\date\\date.txt";
std::string filePathWrite = "D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab1\\Lab1_JAVA\\src\\main\\java\\org\\example\\date\\output.txt";

std::vector<std::vector<int> > bordareMatrix(const std::vector<std::vector<int> > &matrix, int padSize) {
    int n = matrix.size();
    int m = matrix[0].size();
    std::vector<std::vector<int> > padded(n + 2 * padSize, std::vector<int>(m + 2 * padSize, 0));
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < m; ++j) {
            padded[i + padSize][j + padSize] = matrix[i][j];
        }
    }
    return padded;
}

void printMatrix(const std::vector<std::vector<int>> &matrix) {
    // Implement the matrix printing logic here
}

void runConvolution(void (*convolutionFunc)(const std::vector<std::vector<int>> &, const std::vector<std::vector<int>> &, std::vector<std::vector<int>> &, int, int, int), int numThreads) {
    std::ifstream file(filePathRead);
    if (!file.is_open()) {
        std::cerr << "Failed to open file\n";
        return;
    }
    std::vector<std::vector<int>> F = readMatrix(file);
    std::vector<std::vector<int>> C = readMatrix(file);
    int n = F.size();
    int m = F[0].size();
    int k = C.size();

    if (k % 2 == 0) {
        throw std::invalid_argument("Dimensiunea matricei de convoluție trebuie să fie impara.");
    }

    int padSize = k / 2;
    auto F_padded = bordareMatrix(F, padSize);

    std::vector<std::vector<int>> V(n, std::vector<int>(m));

    auto start = std::chrono::high_resolution_clock::now();
    convolutionFunc(F_padded, C, V, n, m, numThreads);
    auto end = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double, std::milli> duration = end - start;

    std::cout << "Durata: " << duration.count() << " ms\n";

    writeMatrixToFile(V, filePathWrite);
}

void secvential() {
    runConvolution(convolutieSecventiala, 1);
}

void threadOrizontala() {
    runConvolution(convolutieOrizontala, 2);
}

void threadVerticala() {
    runConvolution(convolutieVerticala, 16);
}

void threadBloc() {
    runConvolution(convolutieBloc, 16);
}

int main() {
    secvential();
    //threadOrizontala();
    //threadVerticala();
    //threadBloc();
    return 0;
}