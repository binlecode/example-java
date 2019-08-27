package algorithm;

/**
 * Created by IntelliJ IDEA.
 * User: ble000
 * Date: 5/2/11
 * Time: 8:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class SortUtil {

    public static void printArray(int[] dataArr) {
        for (int i=0; i<dataArr.length; i++) {
            System.out.println("" + i + "\t-th number: " + dataArr[i]);
        }
    }

    public static void initializeArray(int[] dataArr, int bound) {
        for (int i=0; i<dataArr.length; i++) {
            dataArr[i] = (int)(Math.random() * bound);
            System.out.println("" + i + "\t-th number: " + dataArr[i]);
        }


    }

}
