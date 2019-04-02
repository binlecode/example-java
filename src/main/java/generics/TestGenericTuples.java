package generics;

import java.util.Date;

/**
 * Created by ble on 5/5/15.
 */
public class TestGenericTuples {

    public TwoTuple<String, Date> getStringAndDate() {
        Date now = new Date();
        return new TwoTuple<>(now.toString(), now);
    }

    public TwoTuple<String, Long> getStringAndTime() {
        Date now = new Date();
        return new TwoTuple<>(now.toString(), now.getTime());
    }

    public ThreeTuple<String, Date, Long> getStringDateAndTime() {
        Date now = new Date();
        return new ThreeTuple<>(now.toString(), now, now.getTime());
    }

    public static void main(String[] args) {
        TestGenericTuples tgt = new TestGenericTuples();
        TwoTuple<String, Date> tt = tgt.getStringAndDate();
        System.out.println(tt.t1);
        System.out.println(tt.t2);

        TwoTuple<String, Long> ttsl = tgt.getStringAndTime();
        System.out.println(ttsl.t1);
        System.out.println(ttsl.t2);

        ThreeTuple<String, Date, Long> ttt = tgt.getStringDateAndTime();
        System.out.println(ttt.t1);
        System.out.println(ttt.t2);
        System.out.println(ttt.t3);
    }
}

// these tuple generics classes are used to support multiple element return value

class TwoTuple<T1, T2> {
    public final T1 t1;
    public final T2 t2;

    public TwoTuple(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }
}

class ThreeTuple<T1, T2, T3> {
    public final T1 t1;
    public final T2 t2;
    public final T3 t3;

    public ThreeTuple(T1 t1, T2 t2, T3 t3) {
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
    }
}
