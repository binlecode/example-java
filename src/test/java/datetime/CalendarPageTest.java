package datetime;

import datetime.CalendarPage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CalendarPageTest {
	
	@Before
	public void setup() {
		System.out.println("set up " + this.getClass().getName());
	}
	
	@After
	public void destroy() {
		System.out.println("destroy " + this.getClass().getName());
	}
	
	@Test
	public void testCalendarPage() {
		CalendarPage calPg = new CalendarPage();
		calPg.printMonthCalendarPage(1980, 9);
		
	}
	
	

}
