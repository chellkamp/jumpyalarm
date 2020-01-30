package org.hellscode.jumpyalarm.ui.model;

import org.hellscode.jumpyalarm.util.AlarmUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Container for binding converter methods
 */
public class Converter {

    private static String _timeOnlyFormat = "hh:mm aa";


    /**
     * Convert a Date object to a simple time string for display
     * @param val source value
     * @return time string
     */
    public static String dateToTimeString(Date val) {
        String retVal = "";

        if (val != null) {
            retVal = new SimpleDateFormat(_timeOnlyFormat, Locale.getDefault()).format(val);
        }
        return retVal;
    }

    /**
     * Compute a label to display for the date of the next alarm
     * @param date date/time - date is minimum date to start alarm, time is time of day that alarm
     *             will go off
     * @param repeat repeat this alarm?
     * @param daysOfWeek days of week bitmask 0b1 is Sunday, 0b1000000 is Saturday
     * @param enabled entry enabled?
     * @return date of next scheduled alarm, or null
     */
    public static String nextAlarmDateString(
            Date date,
            boolean repeat,
            byte daysOfWeek,
            boolean enabled) {
        String retVal = "Never";

        Date nextAlarm = AlarmUtil.getNextAlarm(date, repeat, daysOfWeek, enabled);
        if (nextAlarm != null) {
            retVal = SimpleDateFormat.getDateInstance().format(nextAlarm);
        }

        return retVal;
    }
}
