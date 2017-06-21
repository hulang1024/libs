#ifndef _INT_STACK_H
#define _INT_STACK_H
typedef struct {
    int *elems;
    int logicalLenth;
    int allocLength;
} IntStack;

void IntStackNew(IntStack *s);
void IntStackDelete(IntStack *s);
void IntStackPush(IntStack *s, int elem);
int IntStackPop(IntStack *s);

#endif // _INT_STACK_H
