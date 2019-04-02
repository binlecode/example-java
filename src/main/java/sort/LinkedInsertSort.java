package sort;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by ble on 3/30/14.
 */
public class LinkedInsertSort {

    public static void main(String[] args) {

        LinkedList<Integer> intList = new LinkedList<Integer>();
        for (int i = 0; i < 10; i++) {
            intList.add(new Random().nextInt(100));
            System.out.println("int: " + intList.get(i));
        }

        int j;
        int valToInsert;
        for (int i = 0; i < intList.size(); i++) {
            j = i;
            valToInsert = intList.remove(i);
            while (j > 0 && intList.get(j - 1) > valToInsert) {
                j--;
            }
            // after while loop, j points to the insertion index position
            intList.add(j, valToInsert);
        }

        for (int i : intList) {
            System.out.println("> int: " + i);
        }


    }

}
