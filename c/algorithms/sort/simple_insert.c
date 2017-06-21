#include "alghelp.h"

void sort(int a[], int n)
{
    int i, j, t;
    for(i = 1; i < n; i++)
    {
        j = i - 1;
        t = a[i];
        while(j >= 0 && t <= a[j]) {
            a[j + 1] = a[j];
            j--;
        }
        a[j + 1] = t;
    }
}

int main()
{
    int a[] = {5,4,2,6,1,2,6,11,0,12};
    int n = sizeof(a) / sizeof(int);
    sort(a, n);
    print_int_array(a, n);

    return 0;
}
