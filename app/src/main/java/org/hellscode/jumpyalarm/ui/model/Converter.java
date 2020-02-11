package org.hellscode.jumpyalarm.ui.model;

import android.view.View;

import androidx.databinding.InverseMethod;

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
     * Convert a Date object to a simple date for display
     * @param val source value
     * @return date string
     */
    public static String dateToDateString(Date val) {
        String retVal = "";

        if (val != null) {
            retVal = SimpleDateFormat.getDateInstance().format(val);
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
        String retVal;

        Date nextAlarm = AlarmUtil.getNextAlarm(date, repeat, daysOfWeek, enabled);
        retVal = dateToDateString(nextAlarm);

        if (retVal == null || retVal.isEmpty()) {
            retVal = "Never";
        }

        return retVal;
    }

    /**
     * Convert a boolean to a visibility status
     * @param show true for show; false for hide
     * @return View.VISIBLE or View.GONE
     */
    public static int showToVisibility(boolean show) {
        return show ? View.VISIBLE : View.GONE;
    }

    @InverseMethod("booleanToDaysOfWeekByte")
    public static boolean daysOfWeekByteToBoolean(byte dayMask, AlarmViewModel vm, byte srcVal) {
        return (dayMask & srcVal) != 0;
    }

    public static byte booleanToDaysOfWeekByte(byte dayMask, AlarmViewModel vm, boolean srcVal) {
        // Note: bitwise operators in Java produce ints if the operands are bytes
        byte daysOfWeek = vm.getDaysOfWeek();

        if (srcVal) {
            daysOfWeek = (byte)((daysOfWeek | dayMask) & 0xff);
        } else {
            daysOfWeek = (byte)(daysOfWeek & ~dayMask & 0xff);
        }

        return daysOfWeek;
    }
}
