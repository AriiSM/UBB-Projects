// #include <iostream>
// #include <fstream>
// #include <chrono>
// #include <cmath>
//
// int original_m, original_n, conv_n, no_threads;
// int padding;
// int** original = nullptr;
// int** conv = nullptr;
//
// void readValues() {
//     std::ifstream in("D:/Ariana/Facultate/SEM5/PPD/LAB/Lab2/Lab2_Java/src/main/java/org/example/data/date.txt");
//     in >> original_m >> original_n >> conv_n >> no_threads;
//     padding = conv_n / 2;
//
//     original = new int*[original_m];
//     conv = new int*[conv_n];
//
//     for (int i = 0; i < original_m; i++) {
//         original[i] = new int[original_n];
//     }
//
//     for (int i = 0; i < conv_n; i++) {
//         conv[i] = new int[conv_n];
//     }
//
//     for (int i = 0; i < conv_n; i++) {
//         for (int j = 0; j < conv_n; j++) {
//             in >> conv[i][j];
//         }
//     }
//
//     for (int i = 0; i < original_m; i++) {
//         for (int j = 0; j < original_n; j++) {
//             in >> original[i][j];
//         }
//     }
//
//     int padded_m = original_m + 2 * padding;
//     int padded_n = original_n + 2 * padding;
//     int **padded_original = new int *[padded_m];
//     for (int i = 0; i < padded_m; i++) {
//         padded_original[i] = new int[padded_n]();
//     }
//
//     for (int i = 0; i < original_m; i++) {
//         for (int j = 0; j < original_n; j++) {
//             padded_original[i + padding][j + padding] = original[i][j];
//         }
//     }
//
//     for (int i = 0; i < original_m; i++) {
//         delete[] original[i];
//     }
//     delete[] original;
//
//     original = padded_original;
//     original_m = padded_m;
//     original_n = padded_n;
//
//     in.close();
// }
//
// void liniar() {
//     int** result = new int*[original_m];
//     for (int i = 0; i < original_m; i++) {
//         result[i] = new int[original_n];
//     }
//
//     for (int i = padding; i < original_m - padding; i++) {
//         for (int j = padding; j < original_n - padding; j++) {
//             int temp = 0;
//
//             for (int k = 0; k < conv_n; k++) {
//                 for (int l = 0; l < conv_n; l++) {
//                     int ki = i + (k - conv_n / 2);
//                     int kj = j + (l - conv_n / 2);
//                     temp += original[ki][kj] * conv[k][l];
//                 }
//             }
//
//             result[i][j] = temp;
//         }
//     }
//
//     // Copy the result back to the original matrix
//     for (int i = padding; i < original_m - padding; i++) {
//         for (int j = padding; j < original_n - padding; j++) {
//             original[i][j] = result[i][j];
//         }
//     }
//
//     for (int i = 0; i < original_m; i++) {
//         delete[] result[i];
//     }
//     delete[] result;
// }
//
// void writeResults() {
//     std::ofstream out("D:/Ariana/Facultate/SEM5/PPD/LAB/Lab2/Lab2_Java/src/main/java/org/example/data/GT.txt");
//     for (int i = padding; i < original_m-padding; i++) {
//         for (int j = padding; j < original_n-padding; j++) {
//             out << original[i][j] << ' ';
//         }
//         out << '\n';
//     }
//     out.close();
// }
//
// void cleanup() {
//     for (int i = 0; i < conv_n; ++i)
//         delete[] conv[i];
//     delete[] conv;
//
//     for (int i = 0; i < original_m; ++i) {
//         delete[] original[i];
//     }
//     delete[] original;
// }
//
// int main() {
//     readValues();
//     auto start = std::chrono::high_resolution_clock::now();
//
//     liniar();
//     writeResults();
//     cleanup();
//
//     auto stop = std::chrono::high_resolution_clock::now();
//     auto duration = std::chrono::duration_cast<std::chrono::microseconds>(stop - start);
//     std::cout << duration.count() << '\n';
//
//     return 0;
// }