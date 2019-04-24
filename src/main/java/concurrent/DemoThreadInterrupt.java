package concurrent;

/**
 * Thread is a flow of operation.
 */
public class DemoThreadInterrupt {

    /**
     * Define a runnable class to loop message until interrupted either programmatically or system SIGNAL (Ctrl-C)
     */
    private static class MessageLoop implements Runnable {
        @Override
        public void run() {
            try {
                for (int i = 0; i < 200; i++) {  // this loop will run for 200s if not interrupted
                    Thread.sleep(1000);
                    threadMessage(">> message loop is running round #" + i);
                }
            } catch (InterruptedException e) {
                threadMessage(">> message loop was interrupted");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {


        Thread t = new Thread(new MessageLoop());

        // need to define Runtime shutdown hook to catch SYSINT interruption (ie Ctrl-C)

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                threadMessage("System interrupt received, terminating MessageLoop thread");
                try {
                    t.interrupt();
                    t.join();
                } catch (Exception ex) {  // this should not happen unless t.interrupt doesn't work (t is zombie)
                    threadMessage("Exception: " + ex.getMessage());
                }
            }
        });

        threadMessage("Starting MessageLoop thread");
        t.start();

        // deadline count down and interrupt the MessageLoop thread
        while (t.isAlive()) {
            // join is dependent on the OS for timing, so you should not
            // assume that join will wait exactly as long as you specify
            t.join(1000);
            threadMessage("Waiting for interruption ...");
            Thread.sleep(5000);
            if (t.isAlive()) {
                threadMessage("Tired of waiting, deadline expired, interrupting MessageLoop thread");
                t.interrupt();
                t.join();
            }
        }
        threadMessage("Finally done");
    }

    static void threadMessage(String message) {
        System.out.format("%s: %s%n", Thread.currentThread().getName(), message);
    }

}
