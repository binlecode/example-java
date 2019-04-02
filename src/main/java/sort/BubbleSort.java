package sort;

import java.util.Random;

/**
 * There's double loop of order n, so Time-O(n^2).
 * There's only one additional variable introduced for swap, so Space-O(1)
 */
public class BubbleSort {

    public static void main(String[] args) {

        int[] intArr = new int[10];

        for (int i = 0; i < intArr.length; i++) {
            intArr[i] = new Random().nextInt(100);
            System.out.println("int: " + intArr[i]);
        }

        int swap;
        for (int i = 0; i < intArr.length; i++) {
            for (int j = i + 1; j < intArr.length; j++) {
                if (intArr[i] > intArr[j]) {
                    swap = intArr[i];
                    intArr[i] = intArr[j];
                    intArr[j] = swap;
                }
            }
        }

        for (int i = 0; i < intArr.length; i++) {
            System.out.println("> int: " + intArr[i]);
        }

    }

}
