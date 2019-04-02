package objectbasic;

/**
 * Created by ble on 3/25/15.
 */
public class TestPrimitiveTypes {


    public int changeVar(int x) {
        x += 1;
        return x;
    }

    public int changeRefVar(Integer x) {
        x += 1;
        return x;
    }

    public static void main(String[] args) {
        TestPrimitiveTypes tpt = new TestPrimitiveTypes();

        int x = 123;
        int y = tpt.changeVar(x);

        System.out.println("y = " + y);
        System.out.println("x = " + x);

        Integer refX = new Integer(x);  // the same as Integer.parseInt(123), or new Integer(123)
        Integer refY = tpt.changeRefVar(refX);  // note the auto boxing at return side !

        System.out.println("refY = " + refY);
        System.out.println("refX = " + refX);

        // what if do this
        refY = tpt.changeRefVar(x);
        System.out.println("refY = " + refY);
        System.out.println("x = " + x);


    }

}
