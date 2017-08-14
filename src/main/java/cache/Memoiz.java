package cache;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.function.Function;

public class Memoiz<P, V> {

    private final ConcurrentHashMap<P, FutureTask<V>> cache = new ConcurrentHashMap<>();
    private final Function<P, V> call;

    public Memoiz(final Function<P, V> call) {
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
