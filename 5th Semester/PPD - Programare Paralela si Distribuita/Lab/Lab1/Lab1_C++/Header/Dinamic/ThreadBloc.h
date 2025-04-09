#ifndef THREADBLOC_H
#define THREADBLOC_H

#include <vector>

class ThreadBloc {
public:
    class MyThread {
    public:
        MyThread(const std::vector<std::vector<int>> &F_padded, const std::vector<std::vector<int>> &C, std::vector<std::vector<int>> &V, int startRow, int endRow, int startCol, int endCol, std::vector<std::vector<int>> &matrixThread);
        void operator()();
    private:
        const std::vector<std::vector<int>> &F_padded;
        const std::vector<std::vector<int>> &C;
        std::vector<std::vector<int>> &V;
        int startRow, endRow, startCol, endCol;
        std::vector<std::vector<int>> &matrixThread;
    };
};

#endif // THREADBLOC_H