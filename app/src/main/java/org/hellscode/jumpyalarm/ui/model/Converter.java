package org.hellscode.jumpyalarm.ui.model;

import java.text.SimpleDateFormat;
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
            retVal = new SimpleDateFormat(_timeOnlyFormat, Locale.US).format(val);
        }

        return retVal;
    }
}
