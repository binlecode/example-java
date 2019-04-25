package concurrent;


import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * Rewriting the callable and runnable example using lambda expression.
 */
public class DemoRunnableAndCallableByLambda {

    public static void main(String[] args) {
        threadMessage("start runnable");
        new Thread(() -> {
            threadMessage("I am started");
            for (int i = 0; i < 10; i++) {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    threadMessage("I am interrupted, but I ignore it and move on");
                }
                threadMessage("I am still running... " + i + "/10");
            }
        }).start();

        ExecutorService ste = Executors.newSingleThreadExecutor();
        threadMessage("start callable");
        Future<String> fs = ste.submit(() -> {
            threadMessage("I am called");
            IntStream.rangeClosed(0, 10).forEach((int idx) -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(300);
                } catch (InterruptedException e) {
                    threadMessage("call has been interrupted, but call ignores it and continue");
                }
                threadMessage("I am still being called... " + idx + "/10");
            });
            return String.format("this is callable [%s] returned result", Thread.currentThread().getName());
        });

        try {
            threadMessage("future returned: " + fs.get());
        } catch (InterruptedException e) {
            threadMessage("call interrupted");
        } catch (ExecutionException e) {
            // do nothing
        }

        // always follow this pattern to shut down executor
        try {
            ste.shutdown();
            ste.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            //
        } finally {
            ste.shutdownNow();
        }
    }

    static void threadMessage(String message) {
        System.out.format("%s: %s%n", Thread.currentThread().getName(), message);
    }
}
