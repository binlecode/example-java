package concurrent;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * ceated by ble on 2/24/14.
 */
public class DemoRunnableAndCallable {

    static class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println("I start running");
            for (int i = 0; i < 10; i++) {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("running is interrupted");
                }
                System.out.printf(Thread.currentThread().getName() + ": i am running alone... %d / %d \n", i, 10);
            }
        }
    }

    // callable is like runnable with return value
    static class MyCallable implements Callable<String> {

        @Override
        public String call() throws Exception {
            String tName = Thread.currentThread().getName();
            System.out.println("this is a long running call");
            IntStream.range(0, 10).forEach((int idx) -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(300);
                } catch (InterruptedException e) {
                    System.out.println(String.format("%s: running call has been interrupted", tName));
                }
                System.out.println(String.format("%s: still running ... %d", tName, idx));
            });
            return String.format("%s: this is the call result returned from thread", Thread.currentThread().getName());
        }
    }

    public static void main(String[] args) {
        new Thread(new MyRunnable()).start();
        System.out.println("myRunnable is running");

        ExecutorService stes = Executors.newSingleThreadExecutor();
        Future<String> fs = stes.submit(new MyCallable());
        try {
            System.out.println("future returned: " + fs.get());
        } catch (InterruptedException e) {
            System.out.println("call interrupted");
        } catch (ExecutionException e) {
            // do nothing
        }

        // always follow this pattern to shut down executor
        try {
            stes.shutdown();
            stes.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            //
        } finally {
            stes.shutdownNow();
        }

    }


}
