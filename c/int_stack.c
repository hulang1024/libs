#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include "int_stack.h"

void IntStackNew(IntStack *s)
{
    s->logicalLenth = 0;
    s->allocLength = 4;
    s->elems = malloc(s->allocLength * sizeof(int));
    assert(s->elems != NULL);
}
void IntStackDelete(IntStack *s)
{
    free(s->elems);
}

void IntStackPush(IntStack *s, int elem)
{
    if(s->logicalLenth == s->allocLength) {
        s->allocLength *= 2; // 采用加倍策略
        s->elems = realloc(s->elems, s->allocLength * sizeof(int));
        assert(s->elems != NULL);
    }
    s->elems[s->logicalLenth] = elem;
    s->logicalLenth++;
}
int IntStackPop(IntStack *s)
{
    assert(s->logicalLenth > 0);
    s->logicalLenth--;
    return s->elems[s->logicalLenth];
}

