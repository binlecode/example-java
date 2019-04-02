package datetime;


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Firstly, avoid the old legacy date-time classes such as java.util.Date whenever possible.
 * They are poorly designed, confusing, and troublesome.
 * They were supplanted by the {@code java.time classes} for for many reasons.
 * <p/>
 * If you must, you can convert to/from java.time types to the old.
 */
public class TestLocalDate2Date {

    public static void main(String[] args) {

        date2LocalDate();

        localDate2Date();
    }

    /**
     * Keep in mind that a java.util.Date is a misnomer as it represents a date plus a time-of-day, in UTC.
     * In contrast, the LocalDate class represents a date-only value without time-of-day and without time zone.
     * A time zone is crucial in determining a date. For any given moment, the date varies around the globe by zone.
     * <p/>
     * Going from {@code java.util.Date} to {@code java.time} means converting to the equivalent class of
     * {@code java.time.Instant}. The Instant class represents a moment on the timeline in UTC with a resolution
     * of nanoseconds (up to nine (9) digits of a decimal fraction).
     * We need to move that {@code java.time.Instant} into a time zone. We apply ZoneId to get a {@code ZonedDateTime}.
     */
    public static void date2LocalDate() {

        Date d = new Date();
        Instant instant = d.toInstant();
        ZoneId z = ZoneId.of("America/Montreal");
        ZonedDateTime zdt = instant.atZone(z);
        LocalDate ld = zdt.toLocalDate();
        System.out.printf("US: %tB %<te, %<tY %n", ld);
    }


    /**
     * To move the other direction, from a {@code java.time.LocalDate} to a {@code java.util.Date} means we are
     * going from a date-only to a date-time. So we must specify a time-of-day.
     * You typically want to go for the first moment of the day. Do not assume that is 00:00:00. Anomalies such as
     * Daylight Saving Time (DST) means the first moment may be another time such as 01:00:00. Let {@code java.time}
     * determine that value by calling {@link LocalDate#atStartOfDay()}.
     */
    public static void localDate2Date() {
        ZoneId z = ZoneId.systemDefault();
//        ZoneId z = ZoneId.of("America/Montreal");
        ZonedDateTime zdt = LocalDate.now().atStartOfDay(z).plusHours(6L).plusMinutes(30L).plusSeconds(45L);

        Instant instant = zdt.toInstant();
        // Convert that Instant to java.util.Date by calling from( Instant ).
        Date d = java.util.Date.from(instant);
        System.out.printf("US: %tB %<te,  %<tY  %<tT %<Tp%n", d);
    }

}
