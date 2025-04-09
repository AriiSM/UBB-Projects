//
// Created by ariis on 10/19/2024.
//

#ifndef THREADORIZONTALA_H
#define THREADORIZONTALA_H

#include <vector>
#include <thread>

class ThreadOrizontala {
public:
    class MyThread {
    public:
        MyThread(const std::vector<std::vector<int> > &F_padded, const std::vector<std::vector<int> > &C,
                 std::vector<std::vector<int> > &V, int startRow, int endRow);

        void operator()();

    private:
        const std::vector<std::vector<int> > &F_padded;
        const std::vector<std::vector<int> > &C;
        std::vector<std::vector<int> > &V;
        int startRow;
        int endRow;
    };
};

#endif // THREADORIZONTALA_H
