package sort;

import java.util.Random;

/**
 * This is direct insertion sort. In the nested while loop the insertion location is
 * defined by the completion of the while loop of element shifting. Thus the time complicity
 * is sensitive to initial data distribution. If initially well ordered, time-O(n), in worst
 * case when initially reverse orderred, time-O(n^2). On average time-O(n^2/K), which is
 * still O(n^2).
 * In terms of space complexity, there's only one additional variable needed for swap, thus
 * space-O(1).
 */
public class InsertSort {

    public static void main(String[] args) {
        int[] intArr = new int[10];

        for (int i = 0; i < intArr.length; i++) {
            intArr[i] = new Random().nextInt(100);
            System.out.println("int: " + intArr[i]);
        }

        int j;
        int valToInsert;
        for (int i = 1; i < intArr.length; i++) {
            j = i;
            valToInsert = intArr[i];

            while (j > 0 && intArr[j - 1] > valToInsert) {
                intArr[j] = intArr[j - 1];  // shift larger element to the high end
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
