package concurrent;

/**
 * ceated by ble on 2/24/14.
 */
public class DemoThreadInterrupt {

    private static class MessageLoop implements Runnable {
        @Override
        public void run() {
            try {
                for (int i = 0; i < 200; i++) {
                    Thread.sleep(1000);
                    threadMessage(">> message loop is running round #" + i);
                }
            } catch (InterruptedException e) {
                threadMessage(">> message loop was interrupted");
            }
        }
    }

    public static void main(String[] args) {
        //fixme: this try-catch won't work as it is not able catch cli terminal ctrl-c
        //fixme: need to define Runtime shutdown hook
        try {
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
        } catch (Exception ie) {
            threadMessage("Got interrupted, abort");
        }
    }

    static void threadMessage(String message) {
        System.out.format("%s: %s%n", Thread.currentThread().getName(), message);
    }

}
