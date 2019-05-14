package concurrent;


import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class DemoSynchronizedVsVolatile {

    /**
     * note this is not an atomic variable, and not thread safe!
     */
    private int syncCounter;

    private static volatile int volaCounter;

    public DemoSynchronizedVsVolatile(int syncCounter) {
        this.syncCounter = syncCounter;
    }

    public static void main(String[] args) {

        demoSynchronized();

        demoVolatile();
    }

    /**
     * Synchronized keyboard denote memory-barrier crossing and thread locking for a block of code
     * In this demo, two synchronized methods in two independent threads are changing the same instance
     * value, {code syncCounter}, they are blocking each other since they share the same lock monitor,
     * the host instance.
     * This mutual blocking between threads by synchronization is a big impact to performance.
     */
    public static void demoSynchronized() {

        DemoSynchronizedVsVolatile demo = new DemoSynchronizedVsVolatile(0);

        // start a thread with periodic check and counter modification
        Executors.newSingleThreadScheduledExecutor()
                .scheduleWithFixedDelay(demo::syncIncreaseCounter,100, 100, TimeUnit.MILLISECONDS);

        Executors.newSingleThreadScheduledExecutor()
                .scheduleWithFixedDelay(demo::syncDecreaseCounter,123, 100, TimeUnit.MILLISECONDS);

        // add a monitoring check in main thread
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(
                        () -> threadPrint("MONITOR => instance variable syncCounter = " + demo.syncCounter),
                        0, 100, TimeUnit.MILLISECONDS);

        //todo: shut down all created thread pools after some run time
    }

    /**
     * synchronize on instance, delay some time and then change counter value
     */
    protected synchronized void syncIncreaseCounter() {
        try {
            threadPrint("counter = " + syncCounter);
            // delay some time and change
            for (var i = 0; i < 2; i++) {
                TimeUnit.MILLISECONDS.sleep(500);
                threadPrint("wait ...");
            }

            if (syncCounter < 100) {
                syncCounter++;
                threadPrint("counter incremented to " + syncCounter);
            } else {
                threadPrint("counter >= 100, stop incrementing");
            }
        } catch (InterruptedException ex) {
        }
    }

    /**
     * synchronize on instance, delay some time and then change counter value
     */
    protected synchronized void syncDecreaseCounter() {
        try {
            threadPrint("counter = " + syncCounter);
            // delay some time then change counter
            for (var i = 0; i < 5; i++) {
                TimeUnit.MILLISECONDS.sleep(500);
                threadPrint("wait ...");
            }

            if (syncCounter > -100) {
                syncCounter--;
                threadPrint("counter reduced to " + syncCounter);
            } else {
                threadPrint("counter <= -100, stop reducing");
            }
        } catch (InterruptedException ex) {
        }
    }

    /**
     * A volatile variable has read/write directly from/to the main memory for all threads.
     * There's no explicit synchronization needed on a volatile variable to cross memory barrier.
     * In this demo, any change on the {@code volaCounter} variable is instantly seen by all
     * threads. Due to interleaving read/write from ADD and SUB threads, the before and after
     * value is not consistently the result of +1/-1 logic within ADD or SUB thread.
     */
    public static void demoVolatile() {
        Executors.newSingleThreadScheduledExecutor()
                .scheduleWithFixedDelay(() -> {  // use lambda as runnable
                    try {
                        threadPrint("    ADD => before, volaCounter: " + volaCounter);
                        for (var i = 0; i < 3; i++) {
                            threadPrint("    ADD => wait ...");
                            TimeUnit.MILLISECONDS.sleep(100);
                        }
                        volaCounter ++;
                        // There could be a change of volaCounter value introduced by other threads
                        // between the above and below two statements.
                        // The reason is this variable is not an Atomic variable, such as AtomInteger.
                        threadPrint("    ADD => after, volaCounter: " + volaCounter);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                },100, 100, TimeUnit.MILLISECONDS);

        Executors.newSingleThreadScheduledExecutor()
                .scheduleWithFixedDelay(() -> {
                    try {
                        threadPrint("  SUB => before, volaCounter: " + volaCounter);
                        for (var i = 0; i < 2; i++) {
                            threadPrint("  SUB => wait ...");
                            TimeUnit.MILLISECONDS.sleep(100);
                        }
                        volaCounter --;
                        threadPrint("  SUB => after, volaCounter: " + volaCounter);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                },123, 100, TimeUnit.MILLISECONDS);

        // add a monitoring check in main thread
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(
                        () -> threadPrint("MONITOR => volatile variable volaCounter = " + volaCounter),
                        0, 100, TimeUnit.MILLISECONDS);

        //todo: shut down all created thread pools after some run time
    }

    private static void threadPrint(String message) {
        System.out.println(Thread.currentThread() + " -> " + message);
    }


}
