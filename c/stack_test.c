#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "stack.h"
#include "fraction.h"

static void freeString(void* elemAddr)
{
    free(*(char**)elemAddr);
}
int main()
{
    Stack s;
    {
        STACK_NEW(&s, int);
        for(int i = 0; i < 50; i++)
            StackPush(&s, &i);
        int t;
        for(int i = 0; i < 50; i++)
        {
            StackPop(&s, &t);
            printf("%d\n", t);
        }
        StackDelete(&s);
    }
    {
        STACK_NEW(&s, float);
        float f;
        f = 3.2;
        StackPush(&s, &f);
        f = 3.3;
        StackPush(&s, &f);
        StackPop(&s, &f);
        printf("%.2f\n", f);
        StackPop(&s, &f);
        printf("%.2f\n", f);
        StackDelete(&s);
    }
    {
        STACK_NEW(&s, struct fraction);
        struct fraction f1 = {.num = 1, .denom = 2};
        struct fraction f2 = {.num = 3, .denom = 4};
        StackPush(&s, &f1);
        StackPush(&s, &f2);
        struct fraction t;
        StackPop(&s, &t);
        fraction_print(&t);
        StackPop(&s, &t);
        fraction_print(&t);
        StackDelete(&s);
    }
    {
        StackNew(&s, sizeof(char*), freeString);
        char* strs[] = {"abc", "efg"};
        for(int i = 0; i < 2; i++)
        {
            char* p = strdup(strs[i]);
            StackPush(&s, &p);
        }
        char* t;
        for(int i = 0; i < 2; i++)
        {
            StackPop(&s, &t);
            printf("%s\n", t);
        }
        StackDelete(&s);
    }
    {
        STACK_NEW(&s, char);
        StackPush(&s, "ab" + 0);
        StackPush(&s, "ab" + 1);
        char t;
        StackPop(&s, &t);
        printf("%c\n", t);
        StackPop(&s, &t);
        printf("%c\n", t);
        StackDelete(&s);
    }
}
