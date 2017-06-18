#ifndef __C_STRING_STACK_H
#define __C_STRING_STACK_H

#include <stdlib.h>

struct string_stack {
    char** data;
    int capacity;
    int size;
    int top;
};

inline void string_stack_init(struct string_stack* stack, int initialCapacity)
{
    stack->capacity = initialCapacity;
    stack->data = (char**)malloc(sizeof(char*) * initialCapacity);
    stack->size = 0;
    stack->top = -1;
}

inline int string_stack_push(struct string_stack* stack, const char* s)
{
    if(stack->size >= stack->capacity)
        return 1;
    stack->top++;
    stack->size++;
    stack->data[stack->top] = (char*)malloc(sizeof(char) * strlen(s) + 1);
    strcpy(stack->data[stack->top], s);
    return 0;
}

inline char* string_stack_pop(struct string_stack* stack)
{
    if(stack->size > 0) {
        char* e = stack->data[stack->top];
        stack->top--;
        stack->size--;
        return e;
    }
    else
        return NULL;
}

inline char* string_stack_peek(struct string_stack* stack)
{
    return stack->top >= 0 ? stack->data[stack->top] : NULL;
}

inline int string_stack_isempty(struct string_stack* stack)
{
    return stack->size <= 0;
}

#endif // __C_STRING_STACK_H

