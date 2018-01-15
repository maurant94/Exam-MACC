package it.uniroma1.dis.exam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by duca on 12/01/18.
 */

public class Utilities {

    //NOT MORE USED, ONLY WITH CALENDAR
    public static long daysBetween(Date startDate, Date endDate) {
        long end = endDate.getTime();
        long start = startDate.getTime();
        return TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));
    }

    //NOT MORE USED, ONLY WITH CALENDAR
    public static long daysBetweenExpires(Date date) {
        Date today = new Date();
        long diff = today.getTime() - date.getTime();
        return TimeUnit.MILLISECONDS.toDays(Math.abs(diff));
    }

    public static List<String> toArrayString(Market[] list) {
        ArrayList<String> retValue = new ArrayList<>();
        if (list != null && list.length > 0) {
            for (Market m: list) {
                if (retValue.indexOf(m.getCnome()) < 0)
                    retValue.add(m.getCnome());
            }
            Collections.sort(retValue);
        }
        return retValue;
    }
}
