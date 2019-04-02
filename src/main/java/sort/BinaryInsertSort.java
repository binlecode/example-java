package sort;

import java.util.Random;

/**
 * Created by ble on 3/30/14.
 */
public class BinaryInsertSort {

    public static void main(String[] args) {
        int[] intArr = new int[10];

        for (int i = 0; i < intArr.length; i++) {
            intArr[i] = new Random().nextInt(100);
            System.out.println("int: " + intArr[i]);
        }

        int low, high, mid;
        int valToInsert;
        int j;
        for (int i = 1; i < intArr.length; i++) {
            valToInsert = intArr[i];

            // use binary search to locate the insert index within the already sorted portion
            low = 0;
            high = i - 1;
            while (low <= high) {
                mid = (low + high) / 2;
                if (valToInsert > intArr[mid]) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
            // after while loop, the insertion index is low value
            j = i;
            while (j > low) {
                intArr[j] = intArr[j - 1];
                j = j - 1;
            }
            // after while loop, current j value is the insertion index location
            intArr[j] = valToInsert;
        }

        for (int i = 0; i < intArr.length; i++) {
            System.out.println("> int: " + intArr[i]);
        }

    }

}
