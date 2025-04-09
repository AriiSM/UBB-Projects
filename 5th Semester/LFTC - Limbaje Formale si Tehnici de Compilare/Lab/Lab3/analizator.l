%{
#include <stdio.h>
#include <string.h>

int linie = 1;
int simbolCount = 0;
int atoms = 0;
FILE *ts_file, *fip_file;

struct tid {
    char s[100];
} tid[2000];  

struct fipStruct {
    char s[100];
    int cod;
    int codts;
} fip[10000];

struct SymbolTable {
    char symbol[100];
    int cod;
};

struct SymbolTable symbols[] = {
    {"endl", 2},
    {"iostream", 3},
    {"int", 4},
    {"float", 5},
    {"struct", 6},
    {"else", 7},
    {"cin", 8},
    {"cout", 9},
    {"if", 10},
    {"while", 11},
    {"return", 12},
    {"==", 13},
    {">", 14},
    {"<", 15},
    {"+", 16},
    {"-", 17},
    {"/", 18},
    {"%", 19},
    {"*", 20},
    {"{", 21},
    {"}", 22},
    {"(", 23},
    {")", 24},
    {";", 25},
    {",", 26},
    {"=", 27},
    {">>", 28},
    {"<<", 29},
    {"daca", 30},
    {"atunci", 31},
    {"altfel", 32},
    {"", 0}  
};

int findValue(const char* s) {
    for (int i = 0; i < 32; i++) {
        if (strcmp(symbols[i].symbol, s) == 0) {
            return symbols[i].cod;
        }
    }
    return -1; // Key not found
}

int findSimbol(struct tid* simbolList, int simbolCount, const char *simbol) {
    int st = 0, dr = simbolCount - 1, mid;
    while (st <= dr) {
        mid = (st + dr) / 2;
        if (strcmp(simbolList[mid].s, simbol) < 0) {
            st = mid + 1;
        } else if (strcmp(simbolList[mid].s, simbol) > 0) {
            dr = mid - 1;
        } else return mid;
    }
    return -1;
}

int getPozToInsert(struct tid* simbolList, int simbolCount, const char *simbol) {
    int st = 0, dr = simbolCount - 1, mid;
    while (st <= dr) {
        mid = (st + dr) / 2;
        if (strcmp(simbolList[mid].s, simbol) < 0) {
            st = mid + 1;
        } else {
            dr = mid - 1;
        }
    }
    return st;
}

int createSimbol(struct tid* simbolList, int* simbolCount, const char *simbol) {
    int poz = getPozToInsert(simbolList, *simbolCount, simbol);
    if (*simbolCount >= 2000) {
        printf("Lista de simboluri a atins limita maximă!\n");
        return -1;
    }
    for (int i = *simbolCount; i > poz; i--) {
        strcpy(simbolList[i].s, simbolList[i - 1].s);
    }
    strcpy(simbolList[poz].s, simbol);
    (*simbolCount)++;
    return poz;
}

void addToTid(const char *s, int cod) {
    if (findSimbol(tid, simbolCount, s) == -1) {
        createSimbol(tid, &simbolCount, s);
    }
    atoms++;
    strcpy(fip[atoms].s, s);
    fip[atoms].cod = cod;
    fip[atoms].codts = -2; 
}

void addToFip(const char *s) {
    atoms++;
    strcpy(fip[atoms].s, s);
    fip[atoms].cod = findValue(s);
    fip[atoms].codts = -1;
}
%}

%option noyywrap

%%

\r\n|\r|\n      { ++linie; }
[\t ]+          ;

"endl"          { addToFip(yytext); }
"iostream"      { addToFip(yytext); }
"int"           { addToFip(yytext); }
"float"         { addToFip(yytext); }
"struct"        { addToFip(yytext); }
"else"          { addToFip(yytext); }
"cin"           { addToFip(yytext); }
"cout"          { addToFip(yytext); }
"if"            { addToFip(yytext); }
"while"         { addToFip(yytext); }
"return"        { addToFip(yytext); }

"=="|">"|"<"    { addToFip(yytext); }
"+"|"-"|"/"|"%"|"*" { addToFip(yytext); }

"{"             { addToFip(yytext); }
"}"             { addToFip(yytext); }
"("             { addToFip(yytext); }
")"             { addToFip(yytext); }
";"             { addToFip(yytext); }
","             { addToFip(yytext); }
"="             { addToFip(yytext); }
">>"            { addToFip(yytext); }
"<<"            { addToFip(yytext); }

"daca"          { addToFip(yytext); }
"atunci"        { addToFip(yytext); }
"altfel"        { addToFip(yytext); }

-?[0-9]+                         { addToTid(yytext, 1); }
-?[0-9]+\.[0-9]+(e|E[+|-][0-9]+)? { addToTid(yytext, 1); }
[a-zA-Z_][a-zA-Z0-9_]*           { addToTid(yytext, 0); }

. { printf("Eroare lexicala la linia %d: %s\n", linie, yytext); }

%%

int main(int argc, char **argv) {
    if (argc > 1) {
        FILE *file = fopen(argv[1], "r");
        if (!file) {
            perror("Nu se poate deschide fisierul");
            return 1;
        }
        yyin = file;
    }

    ts_file = fopen("ts.txt", "w");
    if (!ts_file) {
        perror("Nu se poate deschide fisierul TS");
        return 1;
    }

    fip_file = fopen("fip.txt", "w");
    if (!fip_file) {
        perror("Nu se poate deschide fisierul FIP");
        return 1;
    }

    yylex();

    // Print header for ts.txt
    fprintf(ts_file, "%-20s | %s\n", "Simbol", "Index");
    fprintf(ts_file, "---------------------|------\n");

    for (int i = 0; i < simbolCount; i++) {
        fprintf(ts_file, "%-20s | %d\n", tid[i].s, i);
    }

    // Print header for fip.txt
    fprintf(fip_file, "%-20s | %-4s | %s\n", "Simbol", "Cod", "CodTS");
    fprintf(fip_file, "---------------------|------|------\n");

    for (int i = 1; i <= atoms; i++) {
        if (fip[i].codts == -2) {
            fip[i].codts = findSimbol(tid, simbolCount, fip[i].s);
        }
        if (fip[i].codts == -1) {
            fprintf(fip_file, "%-20s | %-4d | -\n", fip[i].s, fip[i].cod);
        } else {
            fprintf(fip_file, "%-20s | %-4d | %d\n", fip[i].s, fip[i].cod, fip[i].codts);
        }
    }

    fclose(ts_file);
    fclose(fip_file);
    return 0;
}
