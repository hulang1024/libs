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