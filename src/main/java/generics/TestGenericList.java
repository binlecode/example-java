package generics;

import java.util.*;

/**
 * Created by ble on 3/12/14.
 */
public class TestGenericList {
    /**
     * Example of generics method with a generics type without boundary because the underlying
     * logic (collections in this case) does not require type boundary.
     *
     * @param set
     * @param <T>
     */
    public <T> List<T> setToList(Set<T> set) {
        if (set == null) return null;

        List<T> lst = new ArrayList<T>();
        for (T elm : set) {
            lst.add(elm);
        }

        return lst;
    }


    public static void main(String[] args) {

        TestGenericList tg = new TestGenericList();

        Set<String> strSet = new HashSet<String>();
        strSet.add("one");
        strSet.add("two");
        strSet.add("three");

        List<String> strList = tg.setToList(strSet);
        for (String str : strList) {
            System.out.println("got element: " + str);
        }

        IndexableTreeSet<Integer> its = new IndexableTreeSet<Integer>();
        its.add(12);
        its.add(34);
        its.add(7);

        System.out.println("IndexableTreeSet of integer size: " + its.size());
        System.out.println("IndexableTreeSet index of 7: " + its.indexOf(7));


        IndexableTreeSet<String> itstr = new IndexableTreeSet<String>();
        itstr.add("abc");
        itstr.add("xyz");
        itstr.add("mno");

        System.out.println("IndexableTreeSet of integer size: " + itstr.size());
        System.out.println("IndexableTreeSet index of 'kkk': " + itstr.indexOf("kkk"));
        System.out.println("IndexableTreeSet index of 'mno': " + itstr.indexOf("mno"));


    }

}

interface Indexable<T> {
    int indexOf(T elm);
}

/**
 * Example of indexable tree set as a subclass of a generics TreeSet class
 *
 * @param <T> should implement {@link java.lang.Comparable} interface
 */
class IndexableTreeSet<T extends Comparable> extends TreeSet<T> implements Indexable<T> {

    public int indexOf(T elm) {
        if (!this.contains(elm)) return -1;

        int idx = 0;
        Iterator<T> itr = this.iterator();
        while (itr.hasNext()) {
            if ((itr.next()).compareTo(elm) == 0) {
                break;
            }
            idx++;
        }
        return idx;
    }
}
