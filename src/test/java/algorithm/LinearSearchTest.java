package algorithm;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: ble000
 * Date: 4/26/11
 * Time: 8:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class LinearSearchTest {
    @Before
	public void setup() {
		System.out.println("set up " + this.getClass().getName());
	}

	@After
	public void destroy() {
		System.out.println("destroy " + this.getClass().getName());
	}

	@Test
	public void testLinearSearch() {
		System.out.println("--> test linear search algorithm");

	}

}
