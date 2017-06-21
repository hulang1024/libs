#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include "stack.h"

static void grow(Stack* s)
{
    s->allocLength *= 2; // 加倍策略
    s->elems = realloc(s->elems, s->allocLength * s->elemSize);
    assert(s->elems != NULL);
}

void StackNew(Stack* s, int elemSize, StackFreeFunc freeFunc)
{
    s->elemSize = elemSize;
    s->logicalLenth = 0;
    s->allocLength = 4;
    s->elems = malloc(s->allocLength * elemSize);
    assert(s->elems != NULL);
}

void StackDelete(Stack* s)
{
    if(s->freeFunc) {
        for(; s->logicalLenth >= 0; s->logicalLenth--)
            s->freeFunc((char*)s->elems + s->logicalLenth * s->elemSize);
    }
    free(s->elems);
}

void StackPush(Stack* s, void* elemAddr)
{
    if(s->logicalLenth == s->allocLength) {
        grow(s);
    }
    memcpy((char*)s->elems + s->logicalLenth * s->elemSize, elemAddr, s->elemSize);
    s->logicalLenth++;
}

void StackPop(Stack* s, void* elemAddr)
{
    assert(s->logicalLenth > 0);
    s->logicalLenth--;
    memcpy(elemAddr, (char*)s->elems + s->logicalLenth * s->elemSize, s->elemSize);
}
