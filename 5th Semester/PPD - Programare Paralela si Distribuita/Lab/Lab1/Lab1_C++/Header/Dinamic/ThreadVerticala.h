//
// Created by ariis on 10/21/2024.
//

#ifndef THREADVERTICALA_H
#define THREADVERTICALA_H

#include <vector>
#include <thread>

class ThreadVerticala {
public:
    class MyThread {
    public:
        MyThread(const std::vector<std::vector<int>>& F_padded, const std::vector<std::vector<int>>& C, std::vector<std::vector<int>>& V, int startCol, int endCol);
        void operator()();

    private:
        const std::vector<std::vector<int>>& F_padded;
        const std::vector<std::vector<int>>& C;
        std::vector<std::vector<int>>& V;
        int startCol;
        int endCol;
    };
};

#endif // THREADVERTICALA_H
