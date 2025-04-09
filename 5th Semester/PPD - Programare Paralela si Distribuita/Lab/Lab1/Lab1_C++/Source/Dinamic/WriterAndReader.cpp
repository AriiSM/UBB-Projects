#include "WriterAndReader.h"

#include <fstream>
#include <sstream>
#include <vector>

std::vector<std::vector<int>> readMatrix(std::ifstream &file) {
    std::vector<std::vector<int>> matrix;
    std::string line;
    while (std::getline(file, line) && !line.empty()) {
        std::istringstream iss(line);
        std::vector<int> row;
        int value;
        while (iss >> value) {
            row.push_back(value);
        }
        matrix.push_back(row);
    }
    return matrix;
}

void writeMatrixToFile(const std::vector<std::vector<int>> &matrix, const std::string &filename) {
    std::ofstream file(filename);
    for (const auto &row : matrix) {
        for (const auto &value : row) {
            file << value << " ";
        }
        file << "\n";
    }
}
