package concurrent.producerconsumer;

/**
 * Created by ble on 2/24/14.
 */
public class ProducerConsumerDemo {

    public static void main(String[] args) {
        Drop drop = new Drop();

        new Thread(new Producer(drop)).start();
        new Thread(new Consumer(drop)).start();


    }

}
