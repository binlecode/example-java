package lambda;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestLambdaRunnable {


    public static void main(String[] args) {

        Integer nbr = 100;

        /*
         * lambda is great choice for runnable implementation
         */
        Runnable worker = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("current thread: " + threadName);

            System.out.println("number: " + nbr);
            try {
//                Thread.sleep(1000);
                TimeUnit.SECONDS.sleep(1);

            } catch (InterruptedException ex) {
                System.out.println("interrupted!");
            }
            System.out.println("number: " + nbr * 2);
        };

        System.out.println("main started");

        String threadName = Thread.currentThread().getName();
        System.out.println("main thread: " + threadName);

        // now use lambda as runnable wrapped by thread construction

        new Thread(worker).start();
        new Thread(worker).start();
        new Thread(worker).start();

        System.out.println("main moves on");

        // now use lambda as runnable in executor

        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(() -> {
            System.out.println("executor thread: " + Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException ex) {
                System.out.println("interrupted!");
            }
        });

        // use the pattern below for a decent executor service shut down with a grace period
        try {
            es.shutdown();
            es.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        } finally {
            if (!es.isTerminated()) {
                System.err.println("cancel non-finished tasks");
            }
            es.shutdownNow();
            System.out.println("shutdown finished");
        }


    }

}
