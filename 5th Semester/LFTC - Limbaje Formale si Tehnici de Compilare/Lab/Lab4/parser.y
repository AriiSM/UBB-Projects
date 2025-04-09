%{
#include <stdio.h>
#include <stdlib.h>

//generata de flex pentru analiza lexicala
extern int yylex(); 
//fisierul de intrare pentru analiza
extern FILE *yyin;  

extern int lineNumber;
//fct externa pentru erori
extern void yyerror(char *s);

%}

%token INT FLOAT CIN COUT WHILE IF ELSE RETURN USING NAMESPACE STD INCLUDE IOSTREAM
%token ASSIGN GT LT PLUS MINUS MUL DIVID SEMICOLON COMMA OPENPARAN CLOSEPARAN OPENBRACE CLOSEBRACE IN OUT EQUAL
%token DECIMAL REAL_NUMBER ID
%start program

%%

program: INT ID OPENPARAN CLOSEPARAN OPENBRACE lista_instructiuni RETURN DECIMAL SEMICOLON CLOSEBRACE;

lista_instructiuni: instructiune | instructiune lista_instructiuni;

instructiune: declarare | atribuire | intrare | iesire | conditional | ciclare;

declarare: tip ID ASSIGN expresie SEMICOLON | tip ID SEMICOLON;
tip: INT | FLOAT;

atribuire: ID ASSIGN expresie SEMICOLON;

expresie: atom | expresie op atom;
op: PLUS | MINUS | MUL | DIVID | EQUAL;

atom: ID | constant;
constant: DECIMAL | REAL_NUMBER;

intrare: CIN IN ID SEMICOLON;
iesire: COUT OUT expresie SEMICOLON;

conditional: IF OPENPARAN conditie CLOSEPARAN OPENBRACE lista_instructiuni CLOSEBRACE altfel;
altfel: ELSE OPENBRACE lista_instructiuni CLOSEBRACE | /*empty*/;

conditie: expresie relatie expresie;
relatie: LT | GT ;

ciclare: while_loop;
while_loop: WHILE OPENPARAN expresie CLOSEPARAN OPENBRACE lista_instructiuni CLOSEBRACE;

%%

int main(int argc, char *argv[]) {
    ++argv, --argc;

    if (argc > 0)
        yyin = fopen(argv[0], "r");
    else
        yyin = stdin;

    while (!feof(yyin)) {
        yyparse();
    }

    printf("Fisierul este corect sintactic!\n");
    fclose(yyin);
    return 0;
}

void yyerror(char *s) {
    extern char* yytext;
    printf("Eroare sintactica la linia %d: %s\n", lineNumber, s);
    exit(1);
}
