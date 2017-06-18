#include <stdio.h>
#include <string.h>
#include <stdlib.h>

void rotate(void* start, void* mid, void* end)
{
    int frontSize = (char*)mid - (char*)start;
    int backSize = (char*)end - (char*)mid;

    char buf[frontSize];
    memcpy(buf, start, frontSize);
    memmove(start, mid, backSize);
    memcpy((char*)start + backSize, buf, frontSize);
}

void int_array_print(int* arr, int len)
{
    for(int i = 0; i < len; i++)
        printf("%d ", arr[i]);
    printf("\n");
}

int main() {
    int ia[] = {1,2,3,4,5};
    rotate(ia, ia + 2, ia + 5);
    int_array_print(ia, 5);
}
