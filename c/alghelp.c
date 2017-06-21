#include "alghelp.h"

static void _print_int_array(int a[], int n, int l)
{
    int i;
    for(i = 0; i < n - 1; i++)
        printf("%d ", a[i]);
    if(i < n)
        printf("%d", a[i]);
    if(l)
        putchar('\n');
}

void print_int_array(int a[], int n)
{
    _print_int_array(a, n, 0);
}

void println_int_array(int a[], int n)
{
    _print_int_array(a, n, 1);
}

void swap(void * a, void * b, size_t size)
{
    char* buf = malloc(size);
    memcpy(buf, a, size);
    memcpy(a, b, size);
    memcpy(b, buf, size);
    free(buf);
}

void rotate(void* start, void* mid, void* end)
{
    int frontSize = (char*)mid - (char*)start;
    int backSize = (char*)end - (char*)mid;

    char buf[frontSize];
    memcpy(buf, start, frontSize);
    memmove(start, mid, backSize);
    memcpy((char*)start + backSize, buf, frontSize);
}

void* lsearch(void* value,
              void* base,
              int n,
              int size,
              int (*cmp)(void*, void*))
{
    int i;
    for(i = 0; i < n; i++)
    {
        void* elemAddr = (char*)base + i * size;
        if(memcmp(value, elemAddr, size) == 0) {
            return elemAddr;
        }
    }
    return NULL;
}