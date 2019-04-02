package datetime;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;

public class TestNewDateTimeApi {

    public static void main(String[] args) {

        // use instant to represent a point in time
        Instant start = Instant.now();

        // Duration.between is commonly used to compare instants
        Instant end = start.plus(Duration.ofHours(2));

        System.out.println(Duration.between(start, end).toMillis());

        // There are two kinds of human time in the new Java API, local date/time and zoned time.
        // Local date/time has a date and/or time of day, but no associated time zone information.

        LocalDate ld = LocalDate.now();
        System.out.println(ld);
    }


}
