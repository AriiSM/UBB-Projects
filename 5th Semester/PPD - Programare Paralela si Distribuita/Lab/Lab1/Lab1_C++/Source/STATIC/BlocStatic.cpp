// #include <iostream>
// #include <thread>
// #include <vector>
// #include <chrono>
// #include <fstream>
//
// #define N 10000  // Number of rows in the matrix
// #define M 10000  // Number of columns in the matrix
// #define K 5     // Filter size
// #define P 2    // Number of threads
//
// int F[N][M]; // Input matrix
// int C[K][K]; // Convolution filter
// int V[N][M]; // Output matrix
// int F_padded[N + 2][M + 2] = {0}; // Padded input matrix
//
// void readMatricesFromFile(const std::string &filename) {
//     std::ifstream file(filename);
//
//     if (!file) {
//         std::cerr << "Eroare la deschiderea fisierului!" << std::endl;
//         exit(1);
//     }
//
//     for (int i = 0; i < N; ++i) {
//         for (int j = 0; j < M; ++j) {
//             file >> F[i][j];
//         }
//     }
//
//     for (int i = 0; i < K; ++i) {
//         for (int j = 0; j < K; ++j) {
//             file >> C[i][j];
//         }
//     }
// }
//
// void writeMatrixToFile(const std::string &filename) {
//     std::ofstream file(filename);
//
//     if (!file) {
//         std::cerr << "Eroare la deschiderea fisierului!" << std::endl;
//         exit(1);
//     }
//
//     for (int i = 0; i < N; ++i) {
//         for (int j = 0; j < M; ++j) {
//             file << V[i][j] << " ";
//         }
//         file << std::endl;
//     }
// }
//
// void worker(int startRow, int endRow, int startCol, int endCol) {
//     int padSize = K / 2;
//     for (int i = startRow; i < endRow; i++) {
//         for (int j = startCol; j < endCol; j++) {
//             int sum = 0;
//             for (int ki = 0; ki < K; ki++) {
//                 for (int kj = 0; kj < K; kj++) {
//                     sum += F_padded[i + ki - padSize][j + kj - padSize] * C[ki][kj];
//                 }
//             }
//             V[i - padSize][j - padSize] = sum;
//         }
//     }
// }
//
// int main() {
//     readMatricesFromFile("D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab1\\Lab1_JAVA\\src\\main\\java\\org\\example\\date\\date.txt");
//
//     for (int i = 0; i < N; ++i) {
//         for (int j = 0; j < M; ++j) {
//             F_padded[i + 1][j + 1] = F[i][j];
//         }
//     }
//
//     double durations[10];
//     for (int i = 0; i < 10; i++) {
//         auto start = std::chrono::high_resolution_clock::now();
//
//         std::vector<std::thread> threads;
//         int blockSize = N / 2;
//
//         for (int rowBlock = 0; rowBlock < 2; rowBlock++) {
//             for (int colBlock = 0; colBlock < 2; colBlock++) {
//                 int startRow = rowBlock * blockSize + K / 2;
//                 int endRow = startRow + blockSize;
//                 int startCol = colBlock * blockSize + K / 2;
//                 int endCol = startCol + blockSize;
//                 threads.push_back(std::thread(worker, startRow, endRow, startCol, endCol));
//             }
//         }
//
//         for (auto &thread: threads) {
//             thread.join();
//         }
//
//         auto end = std::chrono::high_resolution_clock::now();
//         std::chrono::duration<double, std::milli> duration = end - start;
//         durations[i] = duration.count();
//         std::cout << "Durata: " << durations[i] << " ms\n";
//     }
//
//     double sum = 0;
//     for (double d: durations) {
//         sum += d;
//     }
//     std::cout << "Media: " << sum / 10 << " ms\n";
//
//     writeMatrixToFile("D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab1\\Lab1_JAVA\\src\\main\\java\\org\\example\\date\\output.txt");
//
//     return 0;
// }