#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int main() {
    int i1 = 2;
    int i2 = 3;
    swap(&i1, &i2, sizeof(int));
    printf("%d,%d\n", i1,i2);

    float f1 = 2.2;
    float f2 = 3.3;
    swap(&f1, &f2, sizeof(float));
    printf("%f,%f\n", f1,f2);

    struct fraction sf1 = {.num = 1, .denom = 2};
    struct fraction sf2 = {.num = 3, .denom = 4};
    swap(&sf1, &sf2, sizeof(struct f));
    fraction_printf(&sf1);
    fraction_printf(&sf2);

    char* s1 = strdup("s1");
    char* s2 = strdup("s2");
    swap(&s1, &s2, sizeof(char*));
    printf("%s,%s\n", s1,s2);
}
