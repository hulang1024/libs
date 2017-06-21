#include "alghelp.h"

void sort(int a[], int n)
{
    int i, j;
    int min;
    for(i = 0; i < n; i++)
    {
        min = i;
        for(j = i + 1; j < n; j++)
            if(a[j] < a[min])
                min = j;

        if(min != i)
            swap(a + i, a + min, sizeof(int));
    }
}

int main()
{
    int a[] = {5,3,2,7,1};
    sort(a, 5);
    print_int_array(a, 5);

    return 0;
}
