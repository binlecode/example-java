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

/**
 * In JVM a thread itself can not be reused, aka, it has a mechanical lifecycle management.
 * Each {@code ExecutorSerivce} represents a thread pool. Therefore rather than tying a thread lifecycle to
 * a task, an ExecutorService provides thread management and let programmer focus on how to run a task.
 * <p>
 * {@link java.util.concurrent.Executors} factory methods provide a rich array of thread pools - single threaded,
 * cached, priority based, scheduled/periodic, or fixed size, and the size of the wait queue - to work with
 * tasks (Runnable or Callable).
 * <p>
 * A single task can be assigned to the ExecutorService using several methods, including execute() and submit().
 * To run a collection of tasks, use invokeAny() or invokeAll() for different termination logic.
 */
public class DemoExecutorService {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        demoWorkStealingPool();
        demoSheduledThreadPool();
    }

    /**
     * In general, the ExecutorService will not be automatically destroyed when there is not task to process.
     * It will stay alive and wait for new work to do.
     * <p>
     * In some cases this is very helpful; for example, if an app needs to process tasks which appear on an
     * irregular basis or the quantity of these tasks is not known at compile time.
     * On the other hand, an app could reach its end, but it will not be stopped because a waiting ExecutorService
     * will cause the JVM to keep running.
     * <p>
     * To properly shut down an ExecutorService, we have the shutdown() and shutdownNow() APIs.
     * <p>
     * This method shows the preferred pattern to shutdown a thread pool safely.
     * @param es the ExecutorService instance
     * @param timeoutMills the timeout value in ms
     */
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

    /**
     * Utility method to print current thread info before message.
     * @param msg
     */
    static void threadPrint(String msg) {
        System.out.println(Thread.currentThread().getName() + ": " + msg);
    }

    /**
     * In WorkStealingPool each thread in the pool has its own double-ended queue (aka deque)
     * which stores tasks.
     * <p>
     * This architecture is vital for balancing the thread’s workload with
     * the help of the work-stealing algorithm, where free threads try to “steal” work from deques
     * of busy threads: by default, a worker thread gets tasks from the head of its own deque.
     * When it is empty, the thread takes a task from the tail of the deque of another busy thread
     * or from the global entry queue, since this is where the biggest pieces of work are likely
     * to be located.
     */
    public static void demoWorkStealingPool() throws InterruptedException {
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

        // invokeAll returns a list of Futures thus can be concatenated with stream processing
        wspEs.invokeAll(callables)
                .stream()
                .map(future -> {
                    try {
                        return "future returns: " + future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        return "future doesn't return with error: " + e.getMessage();
                    }
                })
                .forEach(System.out::println);

        safeShutdown(wspEs, 100);
    }

    /**
     * It is recommended to schedule fixed delay tasks when task's running duration is not easily predictable
     */
    public static void demoSheduledThreadPool() throws InterruptedException, ExecutionException {

        // setting scheduled pool size to 1 is equivalent to singleThreadScheduledService

        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);

        // simple scheduling with a specific delay
        ScheduledFuture<String> future = ses.schedule(
                () -> {
                    System.out.println(Thread.currentThread().getName() + "running");
                    return Thread.currentThread().getName();
                }, 2000, TimeUnit.MILLISECONDS   // set delay to 2000 ms
        );

        TimeUnit.MILLISECONDS.sleep(1000);
        assert !future.isDone();  // work is not done after 1000 ms delay

        // Scheduling a task produces a specialized future of type ScheduledFuture which, in addition
        // to Future, provides the method getDelay() to retrieve the remaining delay
        long remainingDelay = future.getDelay(TimeUnit.MILLISECONDS);
        System.out.printf("get future remaining delay: %d ms \n", remainingDelay);

        TimeUnit.MILLISECONDS.sleep(1000);  // after this delay the work should be done

        assert future.isDone(); // this is not a blocking call
        System.out.println(String.format("future returned: ${}", future.get()));

        AtomicInteger runIdx = new AtomicInteger(0);

        // define a runnable by a lambda, and it is safe to be used by multiple schedulers as
        // this is purely functional and with no shared state
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

        ScheduledExecutorService ses2 = Executors.newScheduledThreadPool(3);
        ses2.scheduleWithFixedDelay(fixedRateRun, 0, 500, TimeUnit.MILLISECONDS);

        TimeUnit.MILLISECONDS.sleep(5000);
        safeShutdown(ses, 100);
        safeShutdown(ses2, 100);
    }
}
