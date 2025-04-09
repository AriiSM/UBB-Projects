// #include <iostream>
// #include <cstdio>
// #include <fstream>
// #include <chrono>
// #include <thread>
// #include <cmath>
// #include <barrier>
// #define MAX 10000
//
// int original_m, original_n, conv_n;
// int no_threads;
// int padding;
//
// int **original = nullptr;
// int **conv = nullptr;
//
// void readValues() {
//     std::ifstream in("D:/Ariana/Facultate/SEM5/PPD/LAB/Lab2/Lab2_Java/src/main/java/org/example/data/date.txt");
//     in >> original_m >> original_n >> conv_n >> no_threads;
//     padding = conv_n / 2;
//
//     original = new int *[original_m];
//     conv = new int *[conv_n];
//
//     for (int i = 0; i < original_m; i++)
//         original[i] = new int[original_n];
//     for (int i = 0; i < conv_n; i++)
//         conv[i] = new int[conv_n];
//
//     for (int i = 0; i < conv_n; i++)
//         for (int j = 0; j < conv_n; j++)
//             in >> conv[i][j];
//
//     for (int i = 0; i < original_m; i++)
//         for (int j = 0; j < original_n; j++)
//             in >> original[i][j];
//
//     // Add padding to the original matrix
//     int padded_m = original_m + 2 * padding;
//     int padded_n = original_n + 2 * padding;
//     int **padded_original = new int *[padded_m];
//     for (int i = 0; i < padded_m; i++) {
//         padded_original[i] = new int[padded_n]();
//     }
//
//     // Copy the original matrix into the center of the padded matrix
//     for (int i = 0; i < original_m; i++) {
//         for (int j = 0; j < original_n; j++) {
//             padded_original[i + padding][j + padding] = original[i][j];
//         }
//     }
//
//     // Clean up the original matrix
//     for (int i = 0; i < original_m; i++) {
//         delete[] original[i];
//     }
//     delete[] original;
//
//     // Update the original pointer to point to the padded matrix
//     original = padded_original;
//     original_m = padded_m;
//     original_n = padded_n;
//
//     in.close();
// }
//
//
// void work_lines(int id, int line_start, int line_end, std::barrier<> &barrier) {
//     int boundary_down[original_n];
//     int last_modified[original_n], curent[original_n];
//
//     for (int j = 0; j < original_n; j++) {
//         last_modified[j] = original[line_start - 1][j];
//         boundary_down[j] = original[line_end][j];
//     }
//
//     // printf("Thread arrived: %d\n", id);
//     barrier.arrive_and_wait();
//     // printf("Thread released: %d\n", id);
//
//     for (int i = line_start; i < line_end; i++) {
//         for (int j = padding; j < original_n - padding; j++) {
//             int temp = 0;
//
//             for (int k = 0; k < conv_n; k++) {
//                 for (int l = 0; l < conv_n; l++) {
//                     int ki = i + (k - conv_n / 2);
//                     int kj = j + (l - conv_n / 2);
//
//                     if (ki < line_start) {
//                         temp += last_modified[kj] * conv[k][l];
//                     } else if (ki >= line_end) {
//                         temp += boundary_down[kj] * conv[k][l];
//                     } else {
//                         temp += original[ki][kj] * conv[k][l];
//                     }
//                 }
//             }
//
//             curent[j] = temp;
//         }
//
//         if (i > line_start) {
//             for (int j = padding; j < original_n - padding; j++)
//                 original[i - 1][j] = last_modified[j];
//         }
//
//         for (int j = padding; j < original_n - padding; j++)
//             last_modified[j] = curent[j];
//
//         if (i == line_end - 1) {
//             for (int j = padding; j < original_n - padding; j++)
//                 original[i][j] = curent[j];
//         }
//     }
// }
//
//
// int main() {
//     readValues();
//
//     std::barrier barrier(no_threads);
//     std::ofstream out("D:/Ariana/Facultate/SEM5/PPD/LAB/Lab2/Lab2_Java/src/main/java/org/example/data/output.txt");
//
//     std::thread *threads = new std::thread[no_threads];
//
//
//     auto start = std::chrono::high_resolution_clock::now();
//
//     //Delegate threads
//     int lines_per_thread = (original_m - 2 * padding) / no_threads;
//     int remainder_line = (original_m - 2 * padding) % no_threads;
//     int line_start = padding;
//
//     for (int i = 0; i < no_threads; i++) {
//         int line_end = line_start + lines_per_thread;
//
//         if (remainder_line != 0) {
//             remainder_line--;
//             line_end++;
//         }
//
//
//         if (line_end > original_m - padding)
//             line_end = original_m - padding;
//
//         if (line_start >= original_m - padding)
//             break;
//
//         threads[i] = std::thread(work_lines, i, line_start, line_end, std::ref(barrier));
//
//         line_start = line_end;
//     }
//
//     for (int i = 0; i < no_threads; i++) {
//         if (threads[i].joinable()) {
//             threads[i].join();
//             // printf("Thread joined: %d\n", i);
//         }
//         // else{
//         //     printf("Could not join: %d\n", i);
//         // }
//     }
//
//     for (int i = padding; i < original_m - padding; i++) {
//         for (int j = padding; j < original_n - padding; j++) {
//             out << original[i][j] << ' ';
//         }
//         out << '\n';
//     }
//
//     //in.close();
//     out.close();
//
//     for (int i = 0; i < conv_n; ++i)
//         delete [] conv[i];
//     delete [] conv;
//
//     for (int i = 0; i < original_m; ++i) {
//         delete [] original[i];
//     }
//     delete [] original;
//
//     auto stop = std::chrono::high_resolution_clock::now();
//     auto duration = std::chrono::duration_cast<std::chrono::microseconds>(stop - start);
//     std::cout << duration.count() << '\n';
//
//
//     return 0;
// }
