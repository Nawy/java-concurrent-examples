package executor.completion;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

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
