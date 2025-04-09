#ifndef WRITERANDREADER_H
#define WRITERANDREADER_H

#include <vector>
#include <fstream>

std::vector<std::vector<int>> readMatrix(std::ifstream &file);
void writeMatrixToFile(const std::vector<std::vector<int>> &matrix, const std::string &filename);

#endif // WRITERANDREADER_H

