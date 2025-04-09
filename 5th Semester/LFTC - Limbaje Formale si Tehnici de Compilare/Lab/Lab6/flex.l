%{
#include <stdio.h>
#include <stdlib.h>
#include "parser.tab.h"  // Include header-ul Bison pentru a avea acces la token-uri
int lineNumber = 1;
%}

digit       [0-9]
letter      [a-zA-Z_]
id          {letter}({letter}|{digit})*
number      {digit}+(\.{digit}+)?

%%
"int"                { return INT; }
"cin"                { return CIN; }
"cout"               { return COUT; }
"="                  { return ASSIGN; }
"<<"                 { return OUT; }
">>"                 { return IN; }
"+"                  { return PLUS; }
"-"                  { return MINUS; }
"*"                  { return MUL; }
";"                  { return SEMICOLON; }
","                  { return COMMA; }
{id}                 { yylval.sval = strdup(yytext); return ID; }
{number}             { yylval.ival = atoi(yytext); return DECIMAL; }
\n                   { ++lineNumber; }
[ \t\r]+             { /* Ignore white spaces */ }
.                    { fprintf(stderr, "Eroare lexicala: caracter necunoscut '%s' la linia %d.\n", yytext, lineNumber); }
%%