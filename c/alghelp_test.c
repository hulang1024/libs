#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int int_cmp(void * a, void * b)
{
    return *(int*)a - *(int*)b;
}

int main()
{
    int a[] = {1,5,3,4};
    void* p = lsearch((int*)5, a, 4, sizeof(int), int_cmp);
    printf("%d", *(int*)p);


    int ia[] = {1,2,3,4,5};
    rotate(ia, ia + 2, ia + 5);
    print_int_array(ia, 5);


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
