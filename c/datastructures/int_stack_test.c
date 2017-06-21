#include <stdio.h>
#include "int_stack.h"

int main()
{
    IntStack s;
    IntStackNew(&s);
    int i;
    for(i = 0; i < 5; i++)
        IntStackPush(&s, i);
    for(i = 0; i < 5; i++)
        printf("%d\n", IntStackPop(&s));
    IntStackDelete(&s);

}
