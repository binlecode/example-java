package sort;

import java.util.Random;

/**
 * Created by ble on 3/31/14.
 */
public class QuickSort {

    /**
     * Quick sort is swapping based sort, by moving elements to left or right of a pivot value.
     * The number of movements is sensitive to array data distribution and how close the pivot value is to the
     * medium value of the array, thus it is not stable.
     * Its sorting cost average is O(N*log2(N)), worst case is O(N^2), which is degraded to bubble sort.
     *
     * @param arr
     * @param low
     * @param high
     */
    static void qsort(int[] arr, int low, int high) {
        if (low >= high) return;   // only one record

        int i = low;
        int j = high;
        int v = arr[low];   // we pick the first element as pivot value for compare
        while (i < j) {
            while (arr[j] > v && i < j) j--;
            // upon while loop exit, current arr[j] < v or i == j
            if (i < j) arr[i] = arr[j];  // fill arr[j] into vacant i location
            while (arr[i] < v && i < j) i++;
            // upon while loop exit, current arr[i] > v or i == j
            if (i < j) arr[j] = arr[i];  // fill arr[i] into vacant j location
        }
        // upon while loop exit, i = j, which is where v should go
        arr[i] = v;

        // now i(=j) becomes the separation point
        qsort(arr, low, i - 1);   // sort lower part
        qsort(arr, i + 1, high);  // sort higher part
    }


    public static void main(String[] args) {
        int arrSize = 10;
        int[] intArr = new int[arrSize];

        for (int i = 0; i < intArr.length; i++) {
            intArr[i] = new Random().nextInt(100);
            System.out.println("int: " + intArr[i]);
        }

        qsort(intArr, 0, arrSize - 1);

        for (int i : intArr) {
            System.out.println("> int: " + i);
        }
    }
}
