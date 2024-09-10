#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv, char **envp) {
  printf("Content-type: text/html\n\n");
  printf("Am primit antetele de la browser:<br>");
  for (char **env = envp; *env != 0; env++)
  {
    char *thisEnv = *env;
    printf("%s<br>", thisEnv);
  }
  return 0;
}