package concurrent.producerconsumer;

import java.util.Random;


public class Producer implements Runnable {
    Drop drop;

    public Producer(Drop drop) {
        this.drop = drop;
    }

    @Override
    public void run() {
        Random rdn = new Random();
        String message;

        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(rdn.nextInt(5000));  // wait for a random period of time less than 5 seconds
            } catch (InterruptedException e) {
                // do nothing
            }
            message = "This is message #" + i;
            System.out.println(Thread.currentThread().getName() + " >> " + message);
            drop.put(message);
        }
    }
}
