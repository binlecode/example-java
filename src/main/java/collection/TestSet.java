package collection;

import java.util.*;

/**
 * Created by ble on 5/6/15.
 */
public class TestSet {

    public static void main(String[] args) {
        Set<HashableType> hashSet = new HashSet<HashableType>();
        // fill a hash set
        for (int i = 0; i < 10; i++) {
            hashSet.add(new HashableType(i % 5));
        }

        // fill a linked hash set
        Set<HashableType> linkedHashSet = new LinkedHashSet<HashableType>();
        for (int i = 9; i >= 0; i--) {
            linkedHashSet.add(new ComparableType(i % 5));
        }


        // fill a tree set
        Set<ComparableType> treeSet = new TreeSet<ComparableType>();
        for (int i = 9; i >= 0; i--) {
            treeSet.add(new ComparableType(i % 5));
        }

        System.out.println("hash set: " + hashSet);
        System.out.println("linked hash set: " + linkedHashSet);
        System.out.println("tree set: " + treeSet);
    }
}


class EqualableType {
    int i;

    public EqualableType(int i) {
        this.i = i;
    }

    public boolean equals(Object other) {
        return other instanceof EqualableType && i == ((EqualableType) other).i;
    }
}

class HashableType extends EqualableType {
    public HashableType(int i) {
        super(i);
    }

    public int hashCode() {
        return i;
    }
}

class ComparableType extends HashableType implements Comparable<ComparableType> {
    public ComparableType(int i) {
        super(i);
    }

    public int compareTo(ComparableType other) {
        return other.i > i ? -1 : (other.i == i ? 0 : 1);
    }
}
