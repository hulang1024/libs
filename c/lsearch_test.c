#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int main() {

    int a[] = {1,5,3,4};
    void* p = lsearch((int*)5, a, 4, sizeof(int), int_cmp);
    printf("%d", *(int*)p);
}
