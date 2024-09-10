#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int hexatoint(char c) {
    if ((c >= 'a') && (c <= 'f'))
        return c - 'a' + 10;
    if ((c >= 'A') && (c <= 'F'))
        return c - 'A' + 10;
    return c - '0';
}

void decode(char *s) {
    int i = 0, j;
    while (s[i] != '\0') {
        if (s[i] == '+')
            s[i] = ' ';
        if (s[i] == '%') {
            char c = 16 * hexatoint(s[i + 1]) + hexatoint(s[i + 2]);
            s[i] = c;
            j = i + 1;
            do {
                s[j] = s[j + 2];
                j++;
            } while (s[j] != '\0');
        }
        i++;
    }
}

int main() {
  char input[1000];
  char url[1000];
  strcpy(input,getenv("QUERY_STRING"));
  sscanf(input, "link=%s", url);
  printf("Content-type: text/html\n\n");
  printf("Original string: %s<br>\n", url);
  decode(url);
  decode(url);
  printf("Decoded string: %s", url);
}