
#include <functional> // Include this header for std::hash
#include <thread>
#include "Dinamic/ThreadBloc.h"

ThreadBloc::MyThread::MyThread(const std::vector<std::vector<int>> &F_padded, const std::vector<std::vector<int>> &C,
                               std::vector<std::vector<int>> &V, int startRow, int endRow, int startCol, int endCol,
                               std::vector<std::vector<int>> &matrixThread)
    : F_padded(F_padded), C(C), V(V), startRow(startRow), endRow(endRow), startCol(startCol), endCol(endCol),
      matrixThread(matrixThread) {
}

void ThreadBloc::MyThread::operator()() {
    int k = C.size();
    for (int i = startRow; i < endRow; ++i) {
        for (int j = startCol; j < endCol; ++j) {
            int sum = 0;
            for (int ki = 0; ki < k; ++ki) {
                for (int kj = 0; kj < k; ++kj) {
                    sum += F_padded[i + ki][j + kj] * C[ki][kj];
                }
            }
            V[i][j] = sum;
            matrixThread[i][j] = std::hash<std::thread::id>{}(std::this_thread::get_id()); // Store the thread number
        }
    }
}