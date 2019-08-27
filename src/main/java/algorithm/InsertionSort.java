package algorithm;

/**
 * Created by IntelliJ IDEA.
 * User: ble000
 * Date: 5/2/11
 * Time: 8:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class InsertionSort {

    public void demoInsertionSort() {
        int [] dataArr = new int[100];
        SortUtil.initializeArray(dataArr, 100);
        insertionSort(dataArr);
        SortUtil.printArray(dataArr);
    }

    /**
     * An improved version of insertion sort uses the input array to hold both the sorted
     * portion (unshaded) and the numbers still to be inserted (shaded).
     * In each step, the next available unsorted number is inserted into the sorted region
     * of the array.
     * @param dataArr
     */
    public static void insertionSort(int[] dataArr) {
        System.out.println("sorting with insertion sort algorithm");
        int i, j;
        for (i=1; i<dataArr.length; i++) {
            int target = dataArr[i];
            for (j=i-1; (j>=0 && dataArr[j]>target); j--) {
                dataArr[j+1] = dataArr[j]; // shift right
            }
            dataArr[j+1] = target;
        }
    }

    public static void main(String[] args) {
        InsertionSort is = new InsertionSort();
        is.demoInsertionSort();

    }

}
