<h1>Exemplul 6 – Afisarea antetelor din cadrul cererii HTTP</h1>
See it in action

Intrebare: Unde puteti gasi si cum puteti vizualiza codul sursa al exemplului 6 de mai sus?
Raspuns: Ar trebui sa stiti pana in acest moment pe baza cunostintelor dobandite :).

 <h1>Exemplul 7 – URL Decoding </h1>

Fisierul `decodeURL.c` se va compila ca `decodeURL.cgi`.

```c
#include <stdio.h>
#include <string.h>
 
int hexatoint(char c) {
  if ((c>='a') && (c<='f'))
    return c - 'a' + 10;
  if ((c>='A') && (c<='F'))
    return c - 'A' + 10;
  return c - '0';
}
 
void decode(char *s) {
  int i = 0, j;
  while (s[i] != 0) {
    if (s[i] == '+')
      s[i] = ' ';
    if (s[i] == '%') {
      char c = 16 * hexatoint(s[i+1]) + hexatoint(s[i+2]);
      s[i] = c;
      j = i + 1;
      do {
        s[j] = s[j + 2];
        j++;
      } while (s[j] != 0);
    }
    i++;
  }  
}
int main() {
  char s[] = "http%3A%2F%2Fwww.cs.ubbcluj.ro%2F%7Ebufny%2Fprogramare%2Dweb%2F";
  printf("Content-type: text/html\n\n");
  printf("Original string: %s<br>\n", s);
  decode(s);
  printf("Decoded string: %s", s);
}

  printf("Decoded string: %s", s);
}
