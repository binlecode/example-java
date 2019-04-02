package collection;

import java.util.*;

/**
 * Created by ble on 4/18/15.
 */
public class TestArrayRecursiveIteration {

    public int solution(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }

        if (arr.length == 0) {
            return 1;
        }

        List<Integer> lst = new ArrayList<Integer>();
        lst.add(arr[0]);  // set first node using arr[0]
        int nextIdx = arr[0];
        setNextValue(arr, nextIdx, lst);

        return lst.size();
    }


    private void setNextValue(int[] arr, int nextIdx, List<Integer> lst) {
        lst.add(arr[nextIdx]);
        nextIdx = arr[nextIdx];

        if (nextIdx == -1) {
            return;
        }

        setNextValue(arr, nextIdx, lst);
    }


    public static void main(String[] args) {
        TestArrayRecursiveIteration s1 = new TestArrayRecursiveIteration();

        int[] testArr = new int[]{1, 4, -1, 3, 2};

        System.out.println(s1.solution(testArr));


    }


}
