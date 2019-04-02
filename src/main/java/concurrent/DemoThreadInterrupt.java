package concurrent;

/**
 * ceated by ble on 2/24/14.
 */
public class DemoThreadInterrupt {

    static void threadMessage(String message) {
        System.out.format("%s: %s%n", Thread.currentThread().getName(), message);
    }

    private static class MessageLoop implements Runnable {
        @Override
        public void run() {
            try {
                for (int i = 0; i < 200; i++) {
                    Thread.sleep(1000);
                    System.out.println(">> message loop is running round #" + i);
                }
            } catch (InterruptedException e) {
                System.out.println(">> message loop was interrupted");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        threadMessage("Starting MessageLoop thread");

        Thread t = new Thread(new MessageLoop());
        t.start();
        threadMessage("Waiting for MessageLoop thread to finish");

        // wait until MessageLoop thread exits
        while (t.isAlive()) {
            // join is dependent on the OS for timing, so you should not
            // assume that join will wait exactly as long as you specify
            t.join(1000);
            threadMessage("I am waiting ...");
            Thread.sleep(5000);
            if (t.isAlive()) {
                threadMessage("Tired of waiting");
                t.interrupt();
                t.join();
            }
        }

        threadMessage("Finally done");
    }

}
