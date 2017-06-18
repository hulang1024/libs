#ifndef _STACK_H
#define _STACK_H

#define STACK_NEW(p, type) \
    StackNew(p, sizeof(type), NULL)
typedef void (*StackFreeFunc)(void* elemAddr);
typedef struct {
    void *elems;
    int elemSize;
    int logicalLenth;
    int allocLength;
    StackFreeFunc freeFunc;
} Stack;

void StackNew(Stack* s, int elemSize, StackFreeFunc freeFunc);
void StackDelete(Stack* s);
void StackPush(Stack* s, void* elemAddr);
void StackPop(Stack* s, void* elemAddr);

#endif // _STACK_H
