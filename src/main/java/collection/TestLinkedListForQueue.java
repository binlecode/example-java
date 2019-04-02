package collection;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Created by ble on 5/6/15.
 */
public class TestLinkedListForQueue {


    public static void main(String[] args) {
        Queue<String> queue = new LinkedList<String>();

        queue.add("foo");
        queue.add("bar");

        assert queue.peek().equals("foo");
        assert queue.poll().equals("foo");
        assert queue.size() == 1;

        assert queue.peek().equals("bar");
        assert queue.poll().equals("bar");
        assert queue.size() == 0;

        assert queue.peek() == null;
        assert queue.poll() == null;
        try {
            queue.remove();
        } catch (Exception ex) {
            assert ex instanceof NoSuchElementException;
        }
    }

}
