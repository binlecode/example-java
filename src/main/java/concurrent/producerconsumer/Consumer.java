package concurrent.producerconsumer;

public class Consumer implements Runnable {

    public static final int WAIT_WINDOW = 5000;
    Drop drop;

    public Consumer(Drop drop) {
        this.drop = drop;
    }

    @Override
    public void run() {
        while (true) {

            try {
                // always take 5 seconds to get ready for next message
                Thread.sleep(WAIT_WINDOW);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("" + Thread.currentThread().getName() + " >> received message: " + drop.take());
        }

    }
}
