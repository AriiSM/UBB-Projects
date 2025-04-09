#include <iostream>
#include <vector>
#include <thread>
#include <cassert>
#include <chrono>

//OBS sa rulez in Release mode nu in Debug !  

int generateRandomNumber(int upperBoundary) {
    return rand() % upperBoundary + 1;
}

void printArray(std::vector<int> a, int n) {
    for (int i = 0; i < n; i++)
        std::cout << a[i] << " ";
    std::cout << "\n";
}


int operation(int a, int b) {
    return a * a * a * a * b * b * b * b * a * a * a * a * b * b * b * b;
}

void task(std::vector<int> &a, std::vector<int> &b, std::vector<int> &c, int start, int end) {
    for (int i = start; i < end; i++) {
        c[i] = operation(a[i], b[i]);
    }
}

int main() {
    std::vector<int> a, b, c, c_parallel;
    int N = 100000000, upperBound = 100;

    //nr de thread uri
    const int P = 4;
    std::vector<std::thread> threads(P);

    for (int i = 0; i < N; i++) {
        a.push_back(generateRandomNumber(upperBound));
        b.push_back(generateRandomNumber(upperBound));
        c.push_back(0);
        c_parallel.push_back(0);
    }

    auto t_start_seq = std::chrono::high_resolution_clock::now();
    task(a, b, c, 0, N);
    auto t_end_seq = std::chrono::high_resolution_clock::now();
    double elapsed_time_ms_seq = std::chrono::duration<double, std::milli>(t_end_seq - t_start_seq).count();
    std::cout << "Secvential: ";
    std::cout << elapsed_time_ms_seq << "\n";
    //printArray(a, N);
    //printArray(b, N);
    //printArray(c, N);


    int start = 0,
            end = N / P,
            rest = N % P;
    auto t_start_par = std::chrono::high_resolution_clock::now();
    for (int i = 0; i < P; i++) {
        if (rest > 0) {
            end++;
            rest--;
        }
        threads[i] = std::thread(task, std::ref(a), std::ref(b), std::ref(c_parallel), start, end);
        start = end;
        end = start + N / P;
    }

    for (int i = 0; i < P; i++) {
        if (threads[i].joinable())
            threads[i].join();
    }
    auto t_end_par = std::chrono::high_resolution_clock::now();
    double elapsed_time_ms_par = std::chrono::duration<double, std::milli>(t_end_par - t_start_par).count();
    std::cout << "Paralel:";
    std::cout << elapsed_time_ms_par << "\n";

    //printArray(c_parallel, N);
    assert(c == c_parallel);

    return 0;
}
