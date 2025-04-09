#ifndef CONVOLUTIE_H
#define CONVOLUTIE_H

#include <vector>

void convolutieSecventiala(const std::vector<std::vector<int>> &F_padded, const std::vector<std::vector<int>> &C, std::vector<std::vector<int>> &V, int n, int m, int numThreads);
void convolutieOrizontala(const std::vector<std::vector<int>> &F_padded, const std::vector<std::vector<int>> &C, std::vector<std::vector<int>> &V, int n, int m, int numThreads);
void convolutieVerticala(const std::vector<std::vector<int>> &F_padded, const std::vector<std::vector<int>> &C, std::vector<std::vector<int>> &V, int n, int m, int numThreads);
void convolutieBloc(const std::vector<std::vector<int>> &F_padded, const std::vector<std::vector<int>> &C, std::vector<std::vector<int>> &V, int n, int m, int numThreads);

#endif // CONVOLUTIE_H

