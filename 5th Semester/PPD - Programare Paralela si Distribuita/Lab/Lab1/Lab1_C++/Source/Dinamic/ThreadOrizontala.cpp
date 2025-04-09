//
// Created by ariis on 10/19/2024.
//


#include <vector>
#include "Dinamic/ThreadOrizontala.h"

ThreadOrizontala::MyThread::MyThread(const std::vector<std::vector<int> > &F_padded,
                                     const std::vector<std::vector<int> > &C, std::vector<std::vector<int> > &V,
                                     int startRow, int endRow)
    : F_padded(F_padded), C(C), V(V), startRow(startRow), endRow(endRow) {
}

void ThreadOrizontala::MyThread::operator()() {
    int k = C.size();
    int m = V[0].size();
    for (int i = startRow; i < endRow; ++i) {
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
