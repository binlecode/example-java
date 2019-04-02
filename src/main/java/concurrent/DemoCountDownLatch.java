package concurrent;


import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Countdown latches are single-use barriers that allow one or more threads to
 * wait for one or more other threads to do something. The sole constructor for
 * CountDownLatch takes an int that is the number of times the countDown method
 * must be invoked on the latch before all waiting threads are allowed to proceed.
 */
public class DemoCountDownLatch {


    static Runnable runnable = () -> {
        System.out.println(Thread.currentThread().getName() + " is running");
        try {
            TimeUnit.MILLISECONDS.sleep((new Random().nextInt(10) * 200L));
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " got error: " + e.getMessage());
        }
    };

    public static void main(String[] args) throws InterruptedException {

        ExecutorService es = Executors.newCachedThreadPool();

        int concurrency = 10;

        CountDownLatch cdlReady = new CountDownLatch(concurrency);
        CountDownLatch cdlStart = new CountDownLatch(1);
        CountDownLatch cdlDone = new CountDownLatch(concurrency);

        for (int i = 0; i < concurrency; i++) {
            // note the difference between execute and submit, which is the binding of exceptions
            // thrown from the task being invoked
            // execute binds the exception to the caller thread, while submit binds to the returned Future
            // and the Future object's .get() will throw it
            es.execute(() -> {  // this is a wrapper runnable calling the defined runnable
                cdlReady.countDown();
                try {
                    cdlStart.await();
                    runnable.run();    // Wait till peers are ready
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    cdlDone.countDown();
                }
            });
        }

        cdlReady.await();  // this is blocking until its countdown to zero by all threads

        // For interval timing, always use System.nanoTime rather than System.currentTimeMillis.
        // System.nanoTime is both more accurate and more precise and is unaffected by adjustments
        // to the system’s real-time clock.
        long start = System.nanoTime();

        cdlStart.countDown();  // this is like a trigger to kick off all threads

        cdlDone.await(); // this is blocking until all threads are done

        long end = System.nanoTime();

        System.out.println("duration (ms): " + (end - start) / 1000000);

        es.shutdown();  // shutdown pool gracefully

    }
}
