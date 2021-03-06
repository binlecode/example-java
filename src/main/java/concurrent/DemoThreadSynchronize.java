package concurrent;

/**
 * ceated by ble on 2/24/14.
 */
public class DemoThreadSynchronize {

    final String mode;
    public int count = 0;
    public int syncCount = 0;

    public DemoThreadSynchronize(String mode) {
        if (mode != null) {
            this.mode = mode;
        } else {
            this.mode = "ASYNC";
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String mode = "ASYNC";
        if (args.length > 0) {
            mode = args[0];
        }
        DemoThreadSynchronize hts = new DemoThreadSynchronize(mode);
        System.out.println("running demo in " + hts.mode + " mode");

        threadMessage("Starting counter 1");
        Thread t1 = new Thread(new Counter(hts));
        t1.start();

        Thread.sleep(1234);

        threadMessage("Starting counter 2");
        Thread t2 = new Thread(new Counter(hts));
        t2.start();
    }

    /**
     * This method dispatches checkAdd thread according to mode.
     * What's interesting for 'both' mode is that in this case the checkAndAdd() won't see
     * inconsistent check and update value either.
     * This is because synchronized syncCheckAndAdd() method forced the instance state synchronization
     * with main memory, including both non-sync and sync count instance field variables.
     * <p>
     * @throws InterruptedException
     */
    public void checkAddDispatcher() throws InterruptedException {
        if ("sync".equalsIgnoreCase(mode)) {
            this.syncCheckAndAdd();
        } else if ("both".equalsIgnoreCase(mode)) {
            this.syncCheckAndAdd();
            this.checkAndAdd();
        } else {
            this.checkAndAdd();
        }
    }

    /**
     * this count adding method is not synchronized thus not thread safe.
     */
    public void checkAndAdd() throws InterruptedException {
        int origCount = count;
        Thread.sleep(1000);

        // this is not thread safe!
        count += 1;

        // the printed message may show count value difference > 1 after add statement
        // because count value may already be changed by other threads during the delay or
        // after the add statement
        threadMessage(" >> check count = " + origCount + ", after delay, added count = " + count);

        // and the > 1 check may also use a count value that is changed by other threads
        // after previous message print statement
        if (count - origCount > 1) {
            threadMessage(" >>>>>>>> non-atomic change due to lack of synchronization !!!");
        }
    }

    /**
     * synchronized keyword puts a monitor lock on object associated to
     * this method, which in this case is the DemoThreadSynchronize instance.
     */
    public synchronized void syncCheckAndAdd() throws InterruptedException {
        int origSyncCount = syncCount;
        Thread.sleep(1000);
        syncCount += 1;
        threadMessage(" SYNCHRONIZED >> check count = " + origSyncCount + ", after delay, added count = " + syncCount);
        if (syncCount - origSyncCount > 1) {
            threadMessage(" >>>>>>>>  non-atomic change due to lack of synchronization !!!");
        }
    }

    static void threadMessage(String message) {
        System.out.format("thread %s: %s%n", Thread.currentThread().getName(), message);
    }

    /**
     * Counter is a Runnable that holds a reference to main demo class instance.
     */
    private static class Counter implements Runnable {
        private final DemoThreadSynchronize hts;

        public Counter(DemoThreadSynchronize hts) {
            this.hts = hts;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep(1000);
                    hts.checkAddDispatcher();
                }
            } catch (InterruptedException e) {
                threadMessage(" >> was interrupted");
            }
        }
    }


}
