#pragma once
#ifndef MANAGEMENT_H
#define MANAGEMENT_H


// Function declarations (prototypes)
vector<int> generateRandomVector(int size);
void saveVectorToFile(const vector<int>& vector, const char* fileName);
vector<int> readVectorFromFile(const char* fileName);
vector<int> readVectorFromFileManipulated(const char* fileName, int start, int end);
vector<int> reverseVector(const vector<int>& vector);
vector<int> readLargeNumber(const string& filename);
void createvectors();
void printVector(const vector<int>& vec);
vector<int> removeLeadingZeros(const vector<int>& vec);

std::vector<int> reverseVector(const std::vector<int>& vec);
std::pair<std::vector<int>, int> addLargeNumbers(const std::vector<int>& vec1, const std::vector<int>& vec2, int initialCarry);


#endif
