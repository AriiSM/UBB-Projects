
#include <cmath>
#include <iostream>
#include <thread>
#include <vector>
#include "Dinamic/ThreadBloc.h"

void convolutieSecventiala(const std::vector<std::vector<int>> &F_padded, const std::vector<std::vector<int>> &C, std::vector<std::vector<int>> &V, int n, int m, int numThreads) {
    int k = C.size();
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < m; ++j) {
            int sum = 0;
            for (int ki = 0; ki < k; ++ki) {
                for (int kj = 0; kj < k; ++kj) {
                    sum += F_padded[i + ki][j + kj] * C[ki][kj];
                }
            }
            V[i][j] = sum;
        }
    }
}

void convolutieOrizontala(const std::vector<std::vector<int>> &F_padded, const std::vector<std::vector<int>> &C, std::vector<std::vector<int>> &V, int n, int m, int numThreads) {
    std::vector<std::thread> workers;
    int rowsPerThread = std::ceil(static_cast<double>(n) / numThreads);
    for (int i = 0; i < numThreads; ++i) {
        int startRow = i * rowsPerThread;
        int endRow = std::min(startRow + rowsPerThread, n);
        workers.emplace_back([&F_padded, &C, &V, startRow, endRow, m]() {
            int k = C.size();
            for (int row = startRow; row < endRow; ++row) {
                for (int col = 0; col < m; ++col) {
                    int sum = 0;
                    for (int ki = 0; ki < k; ++ki) {
                        for (int kj = 0; kj < k; ++kj) {
                            sum += F_padded[row + ki][col + kj] * C[ki][kj];
                        }
                    }
                    V[row][col] = sum;
                }
            }
        });
    }
    for (auto &worker: workers) {
        worker.join();
    }
}

void convolutieVerticala(const std::vector<std::vector<int>> &F_padded, const std::vector<std::vector<int>> &C, std::vector<std::vector<int>> &V, int n, int m, int numThreads) {
    std::vector<std::thread> workers;
    int colsPerThread = std::ceil(static_cast<double>(m) / numThreads);
    for (int i = 0; i < numThreads; ++i) {
        int startCol = i * colsPerThread;
        int endCol = std::min(startCol + colsPerThread, m);
        workers.emplace_back([&F_padded, &C, &V, startCol, endCol, n]() {
            int k = C.size();
            for (int col = startCol; col < endCol; ++col) {
                for (int row = 0; row < n; ++row) {
                    int sum = 0;
                    for (int ki = 0; ki < k; ++ki) {
                        for (int kj = 0; kj < k; ++kj) {
                            sum += F_padded[row + ki][col + kj] * C[ki][kj];
                        }
                    }
                    V[row][col] = sum;
                }
            }
        });
    }
    for (auto &worker: workers) {
        worker.join();
    }
}

void convolutieBloc(const std::vector<std::vector<int>> &F_padded, const std::vector<std::vector<int>> &C, std::vector<std::vector<int>> &V, int n, int m, int numThreads) {
    std::vector<std::thread> workers;
    std::vector<std::vector<int>> matrixThread(n, std::vector<int>(m, 0));
    int blockSize = static_cast<int>(std::sqrt(numThreads));
    int rowsPerBlock = std::ceil(static_cast<double>(n) / blockSize);
    int colsPerBlock = std::ceil(static_cast<double>(m) / blockSize);
    for (int i = 0; i < blockSize; ++i) {
        for (int j = 0; j < blockSize; ++j) {
            int startRow = i * rowsPerBlock;
            int endRow = std::min(startRow + rowsPerBlock, n);
            int startCol = j * colsPerBlock;
            int endCol = std::min(startCol + colsPerBlock, m);
            workers.emplace_back(ThreadBloc::MyThread(F_padded, C, V, startRow, endRow, startCol, endCol, matrixThread));
        }
    }
    for (auto &worker: workers) {
        worker.join();
    }
}

