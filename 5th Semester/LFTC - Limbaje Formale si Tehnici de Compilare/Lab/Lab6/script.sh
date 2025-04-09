#!/bin/bash

bison -d parser.y
flex flex.l
gcc lex.yy.c parser.tab.c -o parser -lfl
./parser < code.txt > program.asm

nasm -f elf32 program.asm -o program.o
ld -m elf_i386 program.o -o program
#nasm -f elf32 -o program.o program.asm
#ld -m elf_i386 -s -o program program.o

#echo -ne "\x03\x00\x00\x00\x05\x00\x00\x00" > input.bin
#cat input.bin | ./program
#./program < input.bin | hexdump -v -e '1/4 "%d\n"'
./program
