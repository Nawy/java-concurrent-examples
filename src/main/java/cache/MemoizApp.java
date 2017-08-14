package cache;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MemoizApp {

    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

        // create our cached computation
        final Memoiz<Long, String> mem = new Memoiz<>(value -> {
            // show how much time we start thread with specific property
            System.out.println("# -> start " + value);
            try {
                Thread.sleep(value * 1000L);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // just concat numbers in one line
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < value; i++) {
                builder.append(String.valueOf(i));
            }
            return builder.toString();
        });

        // thread 1 for 10 seconds
        new Thread(() -> {
            final String res = mem.count(10L);
            System.out.println(String.format("[%s] Res 10 1: %s", formatter.format(LocalDateTime.now()), res));
        }).start();

        // thread 1 for 5 seconds
        new Thread(() -> {
            final String res = mem.count(5L);
            System.out.println(String.format("[%s] Res 5 1: %s", formatter.format(LocalDateTime.now()), res));
        }).start();

        // just wait 5 seconds and start 10 again(before finish previous invocation)
        try {
            Thread.sleep(5000);
        } catch (Exception e) {}

        // we start final thread where we get cached value
        new Thread(() -> {
            final String res = mem.count(10L);
            System.out.println(String.format("[%s] Res 10 2: %s", formatter.format(LocalDateTime.now()), res));
        }).start();

        // just wait 7 seconds (in general after first invocation 12 seconds of waiting)
        // we wait only 2 seconds after finish first invocation
        // and call functions with property 10 after finish with already cached value
        try {
            Thread.sleep(7000);
        } catch (Exception e) {}

        // we start final thread where we get cached value
        new Thread(() -> {
            final String res = mem.count(10L);
            System.out.println(String.format("[%s] Res 10 3: %s", formatter.format(LocalDateTime.now()), res));
        }).start();
    }
}
