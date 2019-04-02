package sort;

import java.util.Random;

/**
 * Created by ble on 3/30/14.
 */
public class MergeSort {

    static void recurMergeSort(int[] recArr, int[] srcArr, int low, int high) {
        if (low == high) return;

        int mid = (low + high) / 2;   // get mid index with which to cut array in half
        recurMergeSort(recArr, srcArr, low, mid);       // sort first half
        recurMergeSort(recArr, srcArr, mid + 1, high);  // sort second half

        merge(recArr, srcArr, low, mid + 1, high);  // merge sorted arrays
    }


    /**
     * This assumes from low to mid is ordered, and from mid+1 to high is also ordered
     * We can see that merging logic is not sensitive to data distribution, thus the sort is stable.
     * In other words, sorting cost worst case is still at O(N*log2(N))
     *
     * @param srcArr  the source data array
     * @param recArr  the recording array, aka the working cache
     * @param lowPtr  the starting pointer (index) of lower array
     * @param highPtr the starting pointer (index) of higher array
     */
    static void merge(int[] recArr, int[] srcArr, int lowPtr, int highPtr, int upperBound) {
        int lowerBound = lowPtr;
        int mid = highPtr - 1; // index of mid position
        int k = 0;     // index of record arr

        // comparing elements between lower and higer arrays, always pick the smaller ones first
        while (lowPtr <= mid && highPtr <= upperBound) {
            if (srcArr[lowPtr] < srcArr[highPtr]) {
                recArr[k++] = srcArr[lowPtr++];
            } else {
                recArr[k++] = srcArr[highPtr++];
            }
        }

        // in case lower arr still has elements, tail append them since they are already ordered
        while (lowPtr <= mid) {
            recArr[k++] = srcArr[lowPtr++];
        }

        // in case higher arr still has elements, tail append them since they are already ordered
        while (highPtr <= upperBound) {
            recArr[k++] = srcArr[highPtr++];
        }

        // copy calculation result from recorded cache to source array
        for (k = 0; k < (upperBound - lowerBound + 1); k++) {
            srcArr[lowerBound + k] = recArr[k];
        }
    }


    public static void main(String[] args) {
        int arrSize = 10;
        int[] intArr = new int[arrSize];

        for (int i = 0; i < intArr.length; i++) {
            intArr[i] = new Random().nextInt(100);
            System.out.println("int: " + intArr[i]);
        }

        int[] recArr = new int[arrSize];

        recurMergeSort(recArr, intArr, 0, arrSize - 1);

        for (int i : intArr) {
            System.out.println("> int: " + i);
        }
    }


}
