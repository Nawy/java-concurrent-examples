#Cached computation Memorize
```java
public class Memorize<P, V> {

    private final ConcurrentHashMap<P, FutureTask<V>> cache = new ConcurrentHashMap<>();
    private final Function<P, V> call;

    public Memorize(final Function<P, V> call) {
        this.call = call;
    }

    public V count(final P property) {
        while(true) {
            FutureTask<V> f = cache.get(property);

            if (f == null) {
                FutureTask<V> fn = new FutureTask<>(() -> call.apply(property));

                cache.put(property, fn); // add to map before run, it's  important because nex is blocking op
                fn.run(); // blocking operations!!!
                f = fn;
            }

            try {
                return f.get();
            } catch (CancellationException | InterruptedException | ExecutionException e) {
                cache.remove(property);
            }
        }
    }
}
```
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

#Completion Service
```java
public class TextRenderer {

    private final Executor executor;

    public TextRenderer(Executor executor) {
        this.executor = executor;
    }

    public void render(final List<Supplier<String>> operations) {
        CompletionService<String> completionService = new ExecutorCompletionService<>(executor);

        for(final Supplier<String> operation : operations) {
            completionService.submit(operation::get); //callable function
        }

        try {
            for(int i = 0, size = operations.size(); i < size; i++) {
                Future<String> f = completionService.take();
                final String result = f.get();
                System.out.println("Value: " + result);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
```
You will get result : 
```
Value: 4 Render
Value: 2 Render
Value: 1 Render
Value: 3 Render
```
