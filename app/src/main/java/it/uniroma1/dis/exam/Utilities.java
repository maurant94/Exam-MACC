package it.uniroma1.dis.exam;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by duca on 12/01/18.
 */

public class Utilities {
    public static long daysBetween(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));
    }
}
