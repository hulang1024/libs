#ifndef ALGHELP_H
#define ALGHELP_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void print_int_array(int a[], int n);
void println_int_array(int a[], int n);
void* lsearch(void* value,
              void* base,
              int n,
              int size,
              int (*cmp)(void*, void*));

void rotate(void* start, void* mid, void* end);
void swap(void * a, void * b, size_t size);

#endif
