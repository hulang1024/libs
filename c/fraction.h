#include <stdio.h>

struct fraction {
    float num;
    float denom;
};

static void fraction_print(struct fraction * f)
{
    printf("%f/%f\n", f->num, f->denom);
}