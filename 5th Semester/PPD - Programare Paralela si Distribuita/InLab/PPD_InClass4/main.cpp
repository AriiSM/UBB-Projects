#include <iostream>
#include <omp.h>

using namespace std;

const int MAX = 20;
int a[MAX], b[MAX], m[MAX][MAX], c[MAX];

//Produs scalar a doi vectori
int produsScalar(int a[MAX], int b[MAX]) {
    int s = 0;

    for (int i = 0; i < MAX; i++) {
        s += a[i] * b[i];
    }

    return s;
}

//Efectueaza produsul scalar a doi vectori in mod paralel
int produsScalarParallel(int a[MAX], int b[MAX]) {
    int sum = 0;
    int partial_sum = 0;

    //Definim zona paralela
    //Fiecare thread are o copie a variabilei partial_sum prin firstprivate
    //Pornim cu 16 thread-uri
    #pragma omp parallel firstprivate(partial_sum) num_threads(16)
    {
        //By default avem schedule(static)
        //Imparte secventa din "for" in bucati egale pentru fiecare thread
        //Si fiecare thread are propriul partial_sum
        #pragma omp for
        for (int i = 0; i < MAX; i++) {
            partial_sum += a[i] * b[i];
        }

        //Fiecare thread aduna la sum valoarea partial_sum
        #pragma omp critical //Echivalentul lui mutex, doar un thread poate accesa zona critica
        {
            sum += partial_sum;
        }
    }

    return sum;
}

//Face exact ca si produsScalarParallel, doar ca nu mai trebuie sa definim variabila partial_sum
int produsScalarParallel2(int a[MAX], int b[MAX]) {
    int sum = 0;

    //parallel for -> se executa in paralel
    // reduction(+:sum) -> sum este variabila privata pentru fiecare thread, iar la final se face suma lor
    #pragma omp parallel for reduction(+:sum)
    for (int i = 0; i < MAX; i++) {
        sum += a[i] * b[i];
    }

    return sum;
}




void addVectors(int a[MAX], int b[MAX], int c[MAX])
{
    for (int i = 0; i < MAX; i++) {
        c[i] = a[i] + b[i];
    }
}




void multiplyVectors(int a[MAX], int b[MAX], int c[MAX])
{
    for (int i = 0; i < MAX; i++) {
        c[i] = a[i] * b[i];
    }
}



// OpenMp faciliteaza executia folosind thread-uri
//
int main()
{
    //Setam numarul de thread-uri (nu aici se creaza propriu zis thread-urile)
    omp_set_num_threads(4);

    cout << "Thread num1: " << omp_get_num_threads() << endl;

    //Aici se creaza thread-urile si se executa codul din blocul paralel
    //Nu se mai executa 4 thread-uri, ci 16, se suprascrie valoarea de la randul 87 ( omp_set_num_threads(4);)
    #pragma omp parallel num_threads(16)
    {

        cout << "Thread num2: " << omp_get_num_threads() << endl;

        #pragma omp critical //Echivalentul lui mutex, doar un thread poate accesa zona critica
        {
            cout << "Hello from: " << omp_get_thread_num() << endl;
            cout << "Thread num3: " << omp_get_num_threads() << endl;
        }

        cout << "Thread num4: " << omp_get_num_threads() << endl;

        //Directiva "for", se executa in paralel
        //Distribui secventa din "for" pentru fiecare thread ca sa stie ce sa execute 
        //Fiecare thread primeste bucati din "for" cu dimensiunea 2 (schedule(static, 2)) ciclic
        #pragma omp for schedule(static, 2) nowait 
        for (int i = 0; i < MAX; i++) {
            a[i] = omp_get_thread_num();
            b[i] = i;
        }//Prin nowait, thread-ul nu asteapta sa termine toti ceilalti thread-uri, ci continua executia

        //In mod normal daca nu aveam collapse se facea imparteala doar pt primul "for"
        #pragma omp for schedule(static, 2) collapse(2) //collapse(2) -> paralelizare pe 2 nivele
        for (int i = 0; i < MAX; i++) {
            for (int j = 0; j < MAX; j++) {
                m[i][j] = omp_get_thread_num();
            }
        }//Toate thread-urile o sa astepta sa termine toate celelalte thread-uri
    }//Aici avem o bariere implicita

    //Acest for se executa doar de un singur thread
    for (int i = 0; i < MAX; i++) {
        cout << a[i] << " ";
    }

    //Acest for se executa doar de un singur thread
    cout << endl;

    //Acest for se executa doar de un singur thread
    for (int i = 0; i < MAX; i++) {
        for (int j = 0; j < MAX; j++) {
            cout << m[i][j] << " ";
        }
        cout << endl;
    }

    cout << produsScalar(a, b) << endl;
    cout << produsScalarParallel(a, b) << endl;
    cout << produsScalarParallel2(a, b) << endl;

    //Avem 8 thread-uri, fiecare thread executa o sectiune
    //Fiecare sectiune e executata de cate un thread, in acest context (3)
    #pragma omp parallel num_threads(8)
    {
        //Directiva "sections" -> se executa in paralel
        //Fiecare sectiune e executata de cate un thread
        #pragma omp sections
        {
            #pragma omp section
            {
                addVectors(a, b, c);
                #pragma omp critical
                {
                    cout << "Section add: " << omp_get_thread_num() << endl;
                }
            }//Bariera implicita
            #pragma omp section
            {
                multiplyVectors(a, b, c);
                #pragma omp critical
                {
                    cout << "Section multiply: " << omp_get_thread_num() << endl;
                }
            }//Bariera implicita
            #pragma omp section
            {
                produsScalar(a, b);
                #pragma omp critical
                {
                    cout << "Section produs: " << omp_get_thread_num() << endl;
                }
            }//Bariera implicita
        }//Bariera implicita
    }

    //Directiva "single" -> se executa doar de un singur thread, primul thread care e disponibil
    //In Schimb la critical toate thread-urile executa codul, dar doar unul o sa aiba acces la zona critica
    #pragma omp single
    {
        cout << "Doar eu execut: " << omp_get_thread_num() << endl;
    }

    return 0;
}