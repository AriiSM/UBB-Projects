//
// Created by ariis on 10/21/2024.
//

#include "Dinamic/ThreadVerticala.h"

ThreadVerticala::MyThread::MyThread(const std::vector<std::vector<int> > &F_padded,
                                    const std::vector<std::vector<int> > &C, std::vector<std::vector<int> > &V,
                                    int startCol, int endCol)
    : F_padded(F_padded), C(C), V(V), startCol(startCol), endCol(endCol) {
}

void ThreadVerticala::MyThread::operator()() {
    int k = C.size();
    int n = V.size();
    for (int j = startCol; j < endCol; ++j) {
        for (int i = 0; i < n; ++i) {
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
