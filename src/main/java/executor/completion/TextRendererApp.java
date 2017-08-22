package executor.completion;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class TextRendererApp {

    public static void main(String[] args) {
        final TextRenderer textRenderer = new TextRenderer(Executors.newFixedThreadPool(10));

        textRenderer.render(
                Arrays.asList(
                        waitAndReturn(5, "1 Render"),
                        waitAndReturn(2, "2 Render"),
                        waitAndReturn(7, "3 Render"),
                        waitAndReturn(1, "4 Render")
                )
        );
    }

    public static Supplier<String> waitAndReturn(final int seconds, final String value) {
        return () -> {
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return value;
        };
    }
}
