package datetime;

/**
 * ClaendarPage class demos typicall use of calendar class
 * and its methods
 * Method printMonthCalendarPage prints monthly calendar
 * according to input year and month, or use current year
 * and month if input is not given.
 */

import java.util.Calendar;
import java.util.GregorianCalendar;


public class CalendarPage {
	
	private static String[] months = {
		"January",
		"February",
		"March",
		"April",
		"May", 
		"June",
		"July",
		"August",
		"September",
		"October",
		"November",
		"December"
	};
	
	/** The days of each month. */
    private static final int[] dom = {
            31, 28, 31, 30,    /* jan feb mar apr */
            31, 30, 31, 31,    /* may jun jul aug */
            30, 31, 30, 31     /* sep oct nov dec */
    };

	public CalendarPage() {
		// TODO Auto-generated constructor stub
	}
	
	public void printMonthCalendarPage() {
		Calendar c = Calendar.getInstance();
		printMonthCalendarPage(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
	}
	
	public void printMonthCalendarPage(int y, int m) {
		if (m < 1 || m > 12) {
			throw new IllegalArgumentException("Month " + m + " bad, must be 1-12");
		}
		
		System.out.println("  " + y + "  " + months[m-1]);
		System.out.println();
		
		GregorianCalendar calendar = new GregorianCalendar(y, m, 1);
		
		// Compute how much to leave before the first.
        // get(DAY_OF_WEEK  ) returns 0 for Sunday, which is just right.
        int leadGap = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        
        int daysInMonth = dom[m-1];
        if (calendar.isLeapYear(calendar.get(Calendar.YEAR)) && m == 1) {
        	++daysInMonth;
        }

        System.out.println("Su Mo Tu We Th Fr Sa");
        
        // Blank out the labels before 1st day of month
        for (int i=0; i<leadGap; i++) {
        	System.out.print("   ");  // space x 3
        }
        
        for (int i=1; i<=daysInMonth; i++) {
        
        	// This "if" statement is simpler than fiddling with NumberFormat
            if (i<=9) {
            	System.out.print(' ');
            }
            System.out.print(i);
        
            // wrap at the 7th day
            if ((leadGap + i) % 7 == 0) {
            	System.out.println( );
            } else {
            	System.out.print(' ');
            }
        }
		System.out.println();
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CalendarPage cp = new CalendarPage();
		
		if (args.length == 2) {
			cp.printMonthCalendarPage(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		} else {
			cp.printMonthCalendarPage();
		}
		

	}

}
