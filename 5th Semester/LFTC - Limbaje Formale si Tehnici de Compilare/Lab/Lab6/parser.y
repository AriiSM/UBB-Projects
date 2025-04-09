%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

extern int yylex();
extern FILE *yyin;
extern int lineNumber;
void yyerror(char *s);

void generate_asm_header();
void generate_asm_footer();
void generate_asm_read(char *name);
void generate_asm_write(char *name);
void generate_asm_assign(char *name, char *value); // Modificat pentru a accepta char*
void generate_asm_expr(char *op, char *left, char *right);
void generate_int_to_str();
void generate_asm_str_to_int();


int add_symbol(char *name);
char* get_symbol_address(char *name);

#define MAX_SYMBOLS 100
typedef struct {
    char name[50];
    int address;
} Symbol;

Symbol symbol_table[MAX_SYMBOLS];
int symbol_count = 0;

int add_symbol(char *name) {
    for (int i = 0; i < symbol_count; i++) {
        if (strcmp(symbol_table[i].name, name) == 0) {
            return symbol_table[i].address;
        }
    }
    strcpy(symbol_table[symbol_count].name, name);
    symbol_table[symbol_count].address = symbol_count * 4; // Allocate 4 bytes per variable
    return symbol_count++;
}

char* get_symbol_address(char *name) {
    for (int i = 0; i < symbol_count; i++) {
        if (strcmp(symbol_table[i].name, name) == 0) {
            return symbol_table[i].name;
        }
    }
    printf("Eroare: Variabila %s nu este declarata.\n", name);
    exit(1);
}

void generate_asm_header() {
    printf("section .data\n");
    for (int i = 0; i < symbol_count; i++) {
        printf("%s dd 0\n", symbol_table[i].name);
    }
    printf("\n");
    printf("input_buffer db 0,0,0,0,0,0,0,0,0,0 ;\n");
    printf("output_buffer db 0,0,0,0,0,0,0,0,0,0 ;\n");
    printf("\n");
    printf("section .text\n");
    printf("global _start\n");
    printf("\n");
    printf("_start:\n");
}

void generate_asm_footer() {
    printf("    mov eax, 1\n");
    printf("    xor ebx, ebx\n");
    printf("    int 0x80\n");
    generate_int_to_str();
    generate_asm_str_to_int();
}

void generate_asm_read(char *name) {
    // Reading input as a string
    printf("    mov eax, 3                                  ; sys_read\n");
    printf("    mov ebx, 0                                  ; file descriptor 0 (stdin)\n");
    printf("    lea ecx, [input_buffer]                     ; address of buffer\n");
    printf("    mov edx, 10                                 ; max 10 characters (enough for a 32-bit integer)\n");
    printf("    int 0x80                                    ; call kernel\n");
    printf("\n");
    // Convert the input string to an integer
    printf("    lea esi, [input_buffer]                     ; point to the input buffer\n");
    printf("    xor eax, eax                                ; clear eax (this will hold the final integer)\n");
    printf("    call clean_and_convert                      ; clean input and convert string to integer\n");
    printf("    mov [%s], eax                                ; store the converted integer in %s\n", name, name);
    printf("\n");

}

void generate_asm_str_to_int() {
    printf("clean_and_convert:\n");
    printf("                                                ; Clean input and convert string to integer\n");
    printf("    xor eax, eax                                ; clear eax (this will hold the final integer)\n");
    printf("    xor ecx, ecx                                ; clear ecx (used for looping)\n");
    printf("\n");
    printf(".convert_loop:\n");
    printf("    movzx edx, byte [esi + ecx]                 ; load the byte (character) from the input string\n");
    printf("    cmp dl, 0xA                                 ; check if it's a newline ('\\n')\n");
    printf("\n");
    printf("    je .done_conversion                         ; if newline, end of string reached\n");
    printf("    cmp dl, 0xD                                 ; check if it's a carriage return ('\\r')\n");
    printf("\n");
    printf("    je .done_conversion                         ; if carriage return, end of string reached\n");
    printf("    test dl, dl                                 ; check for null terminator\n");
    printf("\n");
    printf("    je .done_conversion                         ; if null, end of string reached\n");
    printf("    sub dl, '0'                                 ; convert ASCII to integer ('0' -> 0, '1' -> 1, etc.)\n");
    printf("    imul eax, eax, 10                           ; multiply eax by 10 to shift left\n");
    printf("    add eax, edx                                ; add the current digit to eax\n");
    printf("    inc ecx                                     ; move to the next character\n");
    printf("\n");
    printf("    jmp .convert_loop                           ; repeat the loop\n");
    printf("\n");
    printf(".done_conversion:\n");
    printf("    ret                                         ; return to the calling function\n");
    printf("\n");
}

void generate_asm_write(char *name) {
    printf("    mov eax, [%s]                                ; Load the value of %s into eax\n", name, name);
    printf("\n");
    printf("    call int_to_str                             ; Convert integer in eax to string\n");
    printf("\n");
    printf("    mov byte [output_buffer + 10], 0            ; Null-terminate after the newline\n");
    printf("    mov byte [output_buffer + 9], 0xA           ; Add newline character at the end\n");
    printf("    mov eax, 4                                  ; sys_write system call\n");
    printf("    mov ebx, 1                                  ; File descriptor 1 (stdout)\n");
    printf("    lea ecx, [output_buffer]                    ; Pointer to the buffer\n");
    printf("    mov edx, 11                                 ; Maximum 11 bytes (integer + newline + null)\n");
    printf("    int 0x80                                    ; Call kernel\n");
    printf("\n");
}

void generate_int_to_str() {
    printf("\n");
    printf("int_to_str:\n");
    printf("    push ebx\n");
    printf("    push ecx\n");
    printf("    push edx\n");
    printf("    mov ecx, output_buffer +9                   ; Start from the last character in the buffer\n");
    printf("    mov byte [ecx], 0                           ; Null-terminate the string\n");
    printf("    mov ebx, 10                                 ; Set the divisor (base 10)\n");
    printf("\n");
    printf(".loop:\n");
    printf("    dec ecx\n");
    printf("    xor edx, edx                                ; Clear remainder\n");
    printf("    div ebx                                     ; Divide eax by 10\n");
    printf("    add dl, '0'                                 ; Convert remainder to ASCII\n");
    printf("    mov [ecx], dl\n");
    printf("    test eax, eax                               ; Check if quotient is zero\n");
    printf("    jnz .loop\n");
    printf("\n");
    printf("    pop edx\n");
    printf("    pop ecx\n");
    printf("    pop ebx\n");
    printf("    ret\n");
    printf("\n");
}

void generate_asm_assign(char *name, char *value) {
    printf("    mov dword [%s], %s\n", name, value);
}

void generate_asm_expr(char *op, char *left, char *right) {
    printf("    mov eax, [%s]\n", left);
    if (strcmp(op, "+") == 0) {
        if (isdigit(right[0])) { // Verifică dacă right este o constantă
            printf("    add eax, %s\n", right); // Folosește direct valoarea constantă
        } else {
            printf("    add eax, [%s]\n", right);
        }
    } else if (strcmp(op, "-") == 0) {
        if (isdigit(right[0])) {//Verifica daca right este o constanta
            printf("    sub eax, %s\n", right); //Foloseste direct valoarea constanta
        } else {
            printf("    sub eax, [%s]\n", right);
        }
    } else if (strcmp(op, "*") == 0) {
        if (isdigit(right[0])) { // Verifică dacă right este o constantă
            printf("    imul eax, %s\n", right); // Folosește direct valoarea constantă
        } else {
            printf("    imul eax, [%s]\n", right);
        }
    }
    printf("    mov dword [%s], eax\n", left); // Se salvează rezultatul în variabila left
}
%}

%union {
    int ival;
    char *sval;
}

%token <ival> DECIMAL
%token <sval> ID
%token INT CIN COUT ASSIGN PLUS MINUS MUL SEMICOLON OUT IN COMMA
%left PLUS MINUS
%left MUL

%start program

%type <sval> instructiune expresie atom lista_variabile declarare

%%

program: lista_instructiuni { generate_asm_footer(); };

lista_instructiuni: instructiune
                  | instructiune lista_instructiuni;

instructiune: declarare { generate_asm_header(); }
             | atribuire { $$ = NULL; }
             | intrare { $$ = NULL; }
             | iesire { $$ = NULL; };

declarare: INT lista_variabile SEMICOLON
         { $$ = NULL; };

lista_variabile: ID { add_symbol($1); }
               | lista_variabile COMMA ID { add_symbol($3); };

atribuire: ID ASSIGN expresie SEMICOLON;
 //{ generate_asm_assign($1, $3); }

expresie: expresie PLUS atom
         { $$ = $1; generate_asm_expr("+", $1, $3); }
         | expresie MINUS atom
         { $$ = $1; generate_asm_expr("-", $1, $3); }
         | expresie MUL atom
         { $$ = $1; generate_asm_expr("*", $1, $3); }
         | atom
         { $$ = $1; };

atom: ID { $$ = get_symbol_address($1); }
    | DECIMAL { 
        char buffer[20]; // Buffer pentru a stoca valoarea numerică ca șir de caractere
        snprintf(buffer, sizeof(buffer), "%d", $1); // Converteste int la șir de caractere
        $$ = strdup(buffer); // Alocă memorie și copiază șirul
      };

intrare: CIN IN ID SEMICOLON
        { generate_asm_read($3); };

iesire: COUT OUT ID SEMICOLON
        { generate_asm_write($3); };

%%

int main(int argc, char *argv[]) {
    ++argv, --argc;

    if (argc > 0) {
        yyin = fopen(argv[0], "r");
        if (!yyin) {
            printf("Eroare: Nu pot deschide fisierul %s\n", argv[0]);
            return 1;
        }
    } else {
        yyin = stdin;
    }

    yyparse();

    fprintf(stderr, "%s", "Fisierul este corect sintactic!\n");
    return 0;
}

void yyerror(char *s) {
    extern char* yytext;
    fprintf(stderr, "Eroare sintactica pentru simbolul %s la linia: %d.\n", yytext, lineNumber);
    exit(1);
}
