void swap(void * a, void * b, size_t size) {
    char* buf = malloc(size);
    memcpy(buf, a, size);
    memcpy(a, b, size);
    memcpy(b, buf, size);
    free(buf);
}

int int_cmp(void * a, void * b)
{
    return *(int*)a - *(int*)b;
}