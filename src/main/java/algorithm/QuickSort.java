package algorithm;

public class QuickSort {

    public void demoQuickSort() {
        int [] dataArr = new int[10];
        SortUtil.initializeArray(dataArr, 10);
        System.out.println("running quicksort");
        quickSort(dataArr);
        System.out.println("sort complete");
        SortUtil.printArray(dataArr);
    }

    public static void quickSort(int[] dataArr) {
        quickSort(dataArr, 0, dataArr.length-1);
    }

    ///////////////////////

    private static void quickSort(int[] data, int idxLow, int idxHigh) {
        System.out.println("current array with idxLow = " + idxLow + ", idxHigh = " + idxHigh);
        SortUtil.printArray(data);

        if (idxHigh - idxLow <= 0) {
            return;
        }

        int pivot = data[idxHigh]; // use highest element as pivot
        System.out.println("--> pivot = " + pivot);

//        int idxPart = partition(data, idxLow, idxHigh, pivot);
        int lowPointer = idxLow - 1; // =idxLow after ++
        int highPointer = idxHigh;   // =idxHigh-1 after --, exclude the pivot, which is chosen at idxHigh above
        while (true) {
            System.out.println("--> loop lowPointer");
            while (data[++lowPointer] < pivot) { // always +1 before each iteration
                System.out.println("-->   lowPointer = " + lowPointer);
            }
            System.out.println("--> loop highPointer");
            while (highPointer > 0 && data[--highPointer] > pivot) { // always -1 before each iteration
                System.out.println("-->   highPointer = "+ highPointer) ;
            }
            if (lowPointer >= highPointer) {
                break;
            } else {
                System.out.println("--> swap location " + lowPointer + " with " + highPointer);
                swap(data, lowPointer, highPointer);
            }
        }
        System.out.println("swap location " + lowPointer + " with " + idxHigh);
        swap(data, lowPointer, idxHigh);  // put pivot in its location

        // lowPointer becomes the partition location
        System.out.println("partition location = " + lowPointer);
        quickSort(data, idxLow, lowPointer-1);  // sort lower partition
        quickSort(data, lowPointer+1, idxHigh); // sort higher partition
    }

    private static void swap(int[] data, int i, int j) {
        int tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    /* this is another modified algorithm version
    function quicksort(lo, hi)
	{
		if(hi <= lo+1) return;

		if((hi - lo) == 2) {
			if(get(hi-1) > get(lo)) exchange(hi-1, lo);
			return;
		}

		var i = lo + 1;
		var j = hi - 1;

		if(get(lo) > get(i)) exchange(i, lo);
		if(get(j) > get(lo)) exchange(lo, j);
		if(get(lo) > get(i)) exchange(i, lo);

		var pivot = get(lo);

		while(true) {
			j--;
			while(pivot > get(j)) j--;
			i++;
			while(get(i) > pivot) i++;
			if(j <= i) break;
			exchange(i, j);
		}
		exchange(lo, j);

		if((j-lo) < (hi-j)) {
			quicksort(lo, j);
			quicksort(j+1, hi);
		} else {
			quicksort(j+1, hi);
			quicksort(lo, j);
		}
	}

     */


    public static void main(String[] args) {
        QuickSort qs = new QuickSort();
        qs.demoQuickSort();

    }

}
