#include "mpi.h"
#include <stdio.h>
#include <stdlib.h>
#include <random>
#include <fstream>
#include <iostream>
#include <vector> 

using namespace std;

// Functie care genereaza un vector de numere random
vector<int> generateRandomVector(int size)
{
    vector<int> vector(size);
    random_device rd;
    mt19937 gen0(rd());
    uniform_int_distribution<> dis0(1, 9);
    vector[0] = dis0(gen0);

    mt19937 gen(rd());
    uniform_int_distribution<> dis(0, 9);
    for (int i = 1; i < size; i++)
    {
        vector[i] = dis(gen);
    }
    return vector;
}

// Functia care salveaza lungimea vectorului pe un rand si pe urmatorul rand continutul vectorului intr-un fisier
void saveVectorToFile(const vector<int>& vectorr, const char* fileName)
{
    FILE* file;
    if (fopen_s(&file, fileName, "w") != 0) {
        cerr << "Eroare la deschiderea fisierului pentru salvare: " << fileName << endl;
        return;
    }
    fprintf(file, "%d\n", vectorr.size());
    for (size_t i = 0; i < vectorr.size(); i++) // Change int to size_t
    {
        fprintf(file, "%d ", vectorr[i]);
    }
    fclose(file);
}

void createvectors()
{
    // Generare si salvare vectori random in fișiere
    saveVectorToFile(generateRandomVector(7), "D:/Ariana/Facultate/SEM5/PPD/LAB/Lab3/Lab3_MPI/MPI/Numar1.txt");
    saveVectorToFile(generateRandomVector(7), "D:/Ariana/Facultate/SEM5/PPD/LAB/Lab3/Lab3_MPI/MPI/Numar2.txt");
}
// Functia care citeste vectorul din fisier (se stie ca pe prima linie in fisier se afla lungimea vectorului)
vector<int> readVectorFromFile(const char* fileName)
{
    FILE* file;
    int size;
    if (fopen_s(&file, fileName, "r") != 0) {
        cerr << "Eroare la deschiderea fisierului pentru citire: " << fileName << endl;
        MPI_Abort(MPI_COMM_WORLD, 1); // Ieșire din MPI dacă fișierul nu se poate deschide
        return {};
    }

    // Citirea dimensiunii vectorului
    if (fscanf_s(file, "%d", &size) != 1) {
        cerr << "Eroare la citirea dimensiunii vectorului." << endl;
        fclose(file);
        MPI_Abort(MPI_COMM_WORLD, 1);
        return {};
    }

    // Alocarea memoriei pentru vector
    vector<int> vector(size);

    // Citirea elementelor vectorului
    for (int i = 0; i < size; i++) {
        if (fscanf_s(file, "%d", &vector[i]) != 1) {
            cerr << "Eroare la citirea elementului " << i << " din vector." << endl;
            fclose(file);
            MPI_Abort(MPI_COMM_WORLD, 1);
            return {};
        }
    }

    fclose(file);

    reverse(vector.begin(), vector.end());
    return vector;
}



vector<int> readVectorFromFileManipulated(const char* fileName, int start, int end)
{
    ifstream file(fileName);
    if (!file.is_open()) {
        cerr << "Eroare la deschiderea fisierului pentru citire: " << fileName << endl;
        MPI_Abort(MPI_COMM_WORLD, 1); // Ieșire din MPI dacă fișierul nu se poate deschide
        return {};
    }

    int size;
    file >> size;  // Citim dimensiunea vectorului (prima linie)

    // Verificăm dacă intervalul este valid
    if (start < 0 || end >= size || start > end) {
        cerr << "Interval invalid: start = " << start << ", end = " << end << endl;
        MPI_Abort(MPI_COMM_WORLD, 1);
        return {};
    }

    // Citim toți elementele din fișier și le stocăm într-un vector
    vector<int> fullVector(size);
    for (int i = 0; i < size; i++) {
        file >> fullVector[i];  // Citim elementele vectorului
    }

    // Inversăm vectorul
    reverse(fullVector.begin(), fullVector.end());

    // Extragem sub-vectorul de la start la end din vectorul inversat
    vector<int> subVector(fullVector.begin() + start, fullVector.begin() + end + 1);

    file.close();
    return subVector;
}



// Functie care inverseaza un vector si returneaza rezultatul
vector<int> reverseVector(const vector<int>& vec) {
    vector<int> reversedVec(vec.size());
    for (size_t i = 0; i < vec.size(); i++) {
        reversedVec[i] = vec[vec.size() - i - 1];
    }
    return reversedVec;
}

void printVector(const vector<int>& vec)
{
    for (const int& val : vec)
    {
        cout << val << " ";
    }
    cout << endl;
}

vector<int> removeLeadingZeros(const vector<int>& vec) {
    // Căutăm primul element diferit de 0
    int index = 0;
    while (index < vec.size() && vec[index] == 0) {
        ++index;
    }

    // Returnăm subvectorul care începe de la primul element diferit de 0
    return vector<int>(vec.begin() + index, vec.end());
}