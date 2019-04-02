package concurrent.producerconsumer;

/**
 * Created by ble on 2/24/14.
 */
public class Drop {
    String message;
    boolean empty = true;

    public synchronized String take() {
        // wait until message is available (not empty)
        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                // do nothing
            }
        }
        empty = true;  // toggle status
        notifyAll();
        return message;
    }

    public synchronized void put(String message) {
        while (!empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                //do nothing
            }
        }
        empty = false;
        this.message = message;
        notifyAll();
    }

}
