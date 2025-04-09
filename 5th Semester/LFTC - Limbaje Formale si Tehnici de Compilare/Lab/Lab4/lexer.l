%{
#include <stdio.h>
#include <stdlib.h>
#include "parser.tab.h"  
int lineNumber = 1;
%}

%%
"int"                { return INT; }
"float"              { return FLOAT; }
"cin"                { return CIN; }
"cout"               { return COUT; }
"while"              { return WHILE; }
"if"                 { return IF; }
"else"               { return ELSE; }
"return"             { return RETURN; }
"="                  { return ASSIGN; }
"=="                 { return EQUAL; }
">"                  { return GT; }
"<<"                 { return OUT; }
">>"                 { return IN; }
"<"                  { return LT; }
"+"                  { return PLUS; }
"-"                  { return MINUS; }
"/"                  { return DIVID; }
"*"                  { return MUL; }
";"                  { return SEMICOLON; }
","                  { return COMMA; }
"("                  { return OPENPARAN; }
")"                  { return CLOSEPARAN; }
"{"                  { return OPENBRACE; }
"}"                  { return CLOSEBRACE; }

[0-9]+               { return DECIMAL; }
[0-9]+\.?[0-9]*      { return REAL_NUMBER; }
[a-zA-Z_]([a-zA-Z_]|[0-9])* { return ID; }
\n                   { ++lineNumber; }
[ \t\r]              { /**/ }
.                    { printf("Eroare lexicala la linia %d: simbol necunoscut '%s'\n", lineNumber, yytext); exit(1); }

%%

int yywrap(void) {
    return 1; //Anunta ca nu mai sunt fisiere de citit
}