package math;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA. User: ble000 Date: 4/27/11 Time: 8:54 AM To change
 * this template use File | Settings | File Templates.
 */
public class ComplexTest {
    Complex c1;
    Complex c2;
    Complex c3;

    @Before
    public void setup() {
        System.out.println("set up " + this.getClass().getName());
        c1 = new Complex(34.5, 56.7);
        c2 = new Complex(22, 59.12);
        c3 = new Complex(-12, -45.3);
    }

    @After
    public void destroy() {
        System.out.println("destroy " + this.getClass().getName());
    }

    @Test
    public void testToString() {
        System.out.println(c1);
    }

    @Test
    public void testMagnitude() {
        System.out.println("magnitude of " + c1 + " = " + c1.magnitude());
    }

    @Test
    public void testRealAndImag() {
        Complex cp = this.c2;
        System.out.println("real part of " + cp + " = " + cp.getReal());
        System.out.println("imag part of " + cp + " = " + cp.getImag());
    }

    // todo: more unit tests

}
