package collection;

/**
 * Created by ble on 4/3/14.
 */
public class TestQueue {

    double[] arr;
    int nItems;
    int front;
    int rear;
    int maxSize;

    public TestQueue(int size) {
        maxSize = size;
        arr = new double[size];
        front = 0;
        rear = -1;
        nItems = 0;
    }

    public void insert(double data) {
        if (rear == maxSize - 1) {
            rear = -1;
        }

        arr[++rear] = data;
        nItems++;
    }

    public double remove() {
        if (nItems == 0) {
            throw new RuntimeException("queue empty");
        }

        double tmp = arr[front++];
        nItems--;

        // this makes a circular array
        if (front > maxSize) {
            front = 0;
        }

        return tmp;
    }

    public double peekFront() {
        return arr[front];
    }

    public int size() {
        return nItems;
    }

    public boolean isEmpty() {
        return nItems == 0;
    }

    public boolean isFull() {
        return nItems == maxSize;
    }


}
