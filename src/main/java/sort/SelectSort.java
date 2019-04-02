package sort;

import java.util.Random;

/**
 * Created by ble on 3/30/14.
 */
public class SelectSort {

    public static void main(String[] args) {
        int[] intArr = new int[10];

        for (int i = 0; i < intArr.length; i++) {
            intArr[i] = new Random().nextInt(100);
            System.out.println("int: " + intArr[i]);
        }

        int k;
        int swap;
        for (int i = 0; i < intArr.length; i++) {
            k = i;
            for (int j = i + 1; j < intArr.length; j++) {
                if (intArr[j] < intArr[k]) {
                    k = j;
                }
            }
            // after for loop, k is the min value index
            System.out.println("k: " + k);
            if (i != k) {
                swap = intArr[i];
                intArr[i] = intArr[k];
                intArr[k] = swap;
            }
        }

        for (int i = 0; i < intArr.length; i++) {
            System.out.println("> int: " + intArr[i]);
        }

    }


}
