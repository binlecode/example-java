package concurrent.executor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DemoExecutorService {

    static void safeShutdown(ExecutorService es, int timeoutMills) {
        try {
            es.shutdown();
            es.awaitTermination(timeoutMills, TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
            // do nothing
        } finally {
            es.shutdownNow();
        }
    }

    static void threadPrint(String msg) {
        System.out.println(Thread.currentThread().getName() + ": " + msg);
    }


    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.out.println("System cpu cores: " + Runtime.getRuntime().availableProcessors());

        // workStealingPool is new since 1.8, a better fixed pool executor default to number of cpu cores
        // rather than fixed pool executor, workStealingPool manages number of worker threads dynamically
        // according to load
        ExecutorService wspEs = Executors.newWorkStealingPool();

        List<Callable<String>> callables = Arrays.asList(
                () -> {
                    String tName = Thread.currentThread().getName();
                    System.out.println(tName + ": Runnable 1 report in");
                    return tName;
                },
                () -> {
                    throw new RuntimeException("Runnable 2's exception");
                },
                () -> {
                    String tName = Thread.currentThread().getName();
                    System.out.println(tName + ": Runnable 3 report in");
                    return tName;
                }
        );

        wspEs.invokeAll(callables)
                .stream()
                .map(future -> {
                    try {
                        return "future returns: " + future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        return "future doesn't return with error: " + e.getMessage();
                    }
                }).forEach(System.out::println);

        safeShutdown(wspEs, 100);

        // setting scheduled pool size to 1 is equivalent to singleThreadScheduledService

        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        ScheduledFuture<String> future = ses.schedule(
                () -> {
                    System.out.println(Thread.currentThread().getName() + "running");
                    return Thread.currentThread().getName();
                }, 2000, TimeUnit.MILLISECONDS
        );

        TimeUnit.MILLISECONDS.sleep(1000);

        // Scheduling a task produces a specialized future of type ScheduledFuture which, in addition
        // to Future, provides the method getDelay() to retrieve the remaining delay
        System.out.printf("get future remaining delay: %d ms \n", future.getDelay(TimeUnit.MILLISECONDS));

        TimeUnit.MILLISECONDS.sleep(1000);  // after sleep the work should be done

        assert future.isDone();
        System.out.println(String.format("future returned: ${}", future.get()));

        AtomicInteger runIdx = new AtomicInteger(0);

        Runnable fixedRateRun = () -> {
            int idx = runIdx.incrementAndGet();
            threadPrint("task " + idx + " running at fixed rate");
            try {
                TimeUnit.MILLISECONDS.sleep(100);  // note that delay is longer than the repetition rate
                threadPrint("task " + idx + " current run complete");
            } catch (InterruptedException e) {
                //
            }
        };

        ses.scheduleAtFixedRate(fixedRateRun, 100, 1000, TimeUnit.MILLISECONDS);

        // it is advisable to schedule fixed delay tasks when task's running duration is not easily predictable

        ScheduledExecutorService ses2 = Executors.newScheduledThreadPool(3);
        ses2.scheduleWithFixedDelay(fixedRateRun, 0, 500, TimeUnit.MILLISECONDS);


//        safeShutdown(ses, 100);


    }
}
