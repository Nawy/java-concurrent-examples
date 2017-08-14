#Cached computation "Memoize"
You will get result :
```
# -> start 5
# -> start 10
[12:49:28.288] Res 5 1: 01234
[12:49:33.258] Res 10 1: 0123456789
[12:49:33.258] Res 10 2: 0123456789
[12:49:35.262] Res 10 3: 0123456789
```
Notice that *start 10* was invoked only once! And after that it returned cached value.
