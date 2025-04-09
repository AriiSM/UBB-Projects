#include <iostream>
#include <fstream>
#include <cuda_runtime.h>
#include <chrono>
#include <vector>

using namespace std;

// Variabile globale pentru dimensiuni
int original_m, original_n, conv_n;
int padding;

// Funcție pentru afișarea unei matrice
void printMatrix(int** matrix, int rows, int cols) {
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            cout << matrix[i][j] << " ";
        }
        cout << endl;
    }
}

// Funcție pentru salvarea unei matrice într-un fișier
void saveMatrixToFile(const string& filename, int** matrix, int rows, int cols) {
    ofstream out(filename);
    if (!out.is_open()) {
        cerr << "Error opening file: " << filename << endl;
        exit(1);
    }

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            out << matrix[i][j] << " ";
        }
        out << endl;
    }

    out.close();
}

// Citirea datelor de intrare și aplicarea bordării (padding)
void readValuesWithPadding(int**& original, int*& padded, int*& conv) {
    ifstream in("input.txt");
    if (!in.is_open()) {
        cerr << "Error opening file: input.txt" << endl;
        exit(1);
    }

    // Citim dimensiunile matricei originale
    in >> original_m >> original_n;

    // Alocăm spațiu pentru matricea originală
    original = new int*[original_m];
    for (int i = 0; i < original_m; i++) {
        original[i] = new int[original_n];
    }

    // Citim valorile matricei originale
    for (int i = 0; i < original_m; i++) {
        for (int j = 0; j < original_n; j++) {
            in >> original[i][j];
        }
    }

    // Citim dimensiunea filtrului (kernel-ul de convoluție)
    in >> conv_n;
    padding = conv_n / 2;

    // Citim kernel-ul de convoluție
    conv = new int[conv_n * conv_n];
    for (int i = 0; i < conv_n; i++) {
        for (int j = 0; j < conv_n; j++) {
            in >> conv[i * conv_n + j];
        }
    }

    // Dimensiunile matricei cu padding
    int padded_m = original_m + 2 * padding;
    int padded_n = original_n + 2 * padding;

    // Creăm matricea cu padding
    padded = new int[padded_m * padded_n]();

    // Copiem matricea originală în centrul matricei cu padding
    for (int i = 0; i < original_m; i++) {
        for (int j = 0; j < original_n; j++) {
            padded[(i + padding) * padded_n + (j + padding)] = original[i][j];
        }
    }

    in.close();

    // Afișăm matricea originală
    cout << "Original matrix:" << endl;
    printMatrix(original, original_m, original_n);

    // Afișăm kernel-ul de convoluție
    cout << "Convolution kernel:" << endl;
    for (int i = 0; i < conv_n; i++) {
        for (int j = 0; j < conv_n; j++) {
            cout << conv[i * conv_n + j] << " ";
        }
        cout << endl;
    }
}

// Kernel CUDA pentru aplicarea convoluției
__global__ void convolutionKernel(int* padded, int* conv, int* original, int padded_m, int padded_n, int conv_n, int original_m, int original_n) {
    int row = blockIdx.y * blockDim.y + threadIdx.y; // Rândul firului
    int col = blockIdx.x * blockDim.x + threadIdx.x; // Coloana firului

    int padding = conv_n / 2;

    if (row < original_m && col < original_n) {
        int sum = 0;

        for (int i = 0; i < conv_n; i++) {
            for (int j = 0; j < conv_n; j++) {
                int neighborRow = row + i;
                int neighborCol = col + j;
                sum += padded[neighborRow * padded_n + neighborCol] * conv[i * conv_n + j];
            }
        }

        // Salvăm rezultatul direct în matricea originală
        original[row * original_n + col] = sum;
    }
}

void compareFiles(const string& file1, const string& file2) {
    ifstream f1(file1), f2(file2);
    if (!f1.is_open()) {
        cerr << "Error opening file: " << file1 << endl;
        return;
    }
    if (!f2.is_open()) {
        cerr << "Error opening file: " << file2 << endl;
        return;
    }

    char buffer1[1024], buffer2[1024];
    int lineNumber = 0;
    bool identical = true;

    while (true) {
        // Citește o linie din fiecare fișier
        f1.getline(buffer1, sizeof(buffer1));
        f2.getline(buffer2, sizeof(buffer2));

        lineNumber++;

        // Verificăm EOF-ul pentru fiecare fișier
        bool eof1 = f1.eof();
        bool eof2 = f2.eof();

        if (eof1 && eof2) {
            break; // Ambele fișiere s-au terminat
        }
        if (eof1 || eof2) {
            cout << "Files have different lengths." << endl;
            identical = false;
            break;
        }

        // Compară liniile
        if (string(buffer1) != string(buffer2)) {
            identical = false;
            cout << "Difference at line " << lineNumber << ":\n";
            cout << "File 1: " << buffer1 << "\n";
            cout << "File 2: " << buffer2 << "\n";
        }
    }

    if (identical) {
        cout << "The files are identical." << endl;
    }

    f1.close();
    f2.close();
}


int main() {
    int **original, *padded, *conv;

    // Citirea datelor de intrare și aplicarea bordării
    readValuesWithPadding(original, padded, conv);

    // Dimensiunile matricei cu padding
    int padded_m = original_m + 2 * padding;
    int padded_n = original_n + 2 * padding;

    // Transformăm matricea 2D originală în vector 1D
    int* original_1d = new int[original_m * original_n];
    for (int i = 0; i < original_m; i++) {
        for (int j = 0; j < original_n; j++) {
            original_1d[i * original_n + j] = original[i][j];
        }
    }

    // Alocare memorie pe GPU
    int *d_padded, *d_conv, *d_original;
    cudaMalloc((void**)&d_padded, padded_m * padded_n * sizeof(int));
    cudaMalloc((void**)&d_conv, conv_n * conv_n * sizeof(int));
    cudaMalloc((void**)&d_original, original_m * original_n * sizeof(int));

    // Copierea datelor pe GPU
    cudaMemcpy(d_padded, padded, padded_m * padded_n * sizeof(int), cudaMemcpyHostToDevice);
    cudaMemcpy(d_conv, conv, conv_n * conv_n * sizeof(int), cudaMemcpyHostToDevice);
    cudaMemcpy(d_original, original_1d, original_m * original_n * sizeof(int), cudaMemcpyHostToDevice);

    // Configurarea grilei și blocurilor
    dim3 threadsPerBlock(4, 4);
    dim3 numBlocks((original_n + threadsPerBlock.x - 1) / threadsPerBlock.x,
                   (original_m + threadsPerBlock.y - 1) / threadsPerBlock.y);

    // Lansarea kernel-ului CUDA
    auto start = chrono::high_resolution_clock::now();
    convolutionKernel<<<numBlocks, threadsPerBlock>>>(d_padded, d_conv, d_original, padded_m, padded_n, conv_n, original_m, original_n);
    cudaDeviceSynchronize();
    auto end = chrono::high_resolution_clock::now();

    // Copierea rezultatelor înapoi în matricea originală
    cudaMemcpy(original_1d, d_original, original_m * original_n * sizeof(int), cudaMemcpyDeviceToHost);

    // Transformăm vectorul 1D în matrice 2D
    for (int i = 0; i < original_m; i++) {
        for (int j = 0; j < original_n; j++) {
            original[i][j] = original_1d[i * original_n + j];
        }
    }

    // Salvarea rezultatului
    saveMatrixToFile("output.txt", original, original_m, original_n);

    // Afișare timp de execuție
    cout << "Execution time: " << chrono::duration_cast<chrono::milliseconds>(end - start).count() << " ms" << endl;

    // Compararea fișierelor
    compareFiles("output.txt", "GT.txt");
    
    // Eliberare memorie
    for (int i = 0; i < original_m; i++) delete[] original[i];
    delete[] original;
    delete[] original_1d;
    delete[] padded;
    delete[] conv;
    cudaFree(d_padded);
    cudaFree(d_conv);
    cudaFree(d_original);

    return 0;
}
