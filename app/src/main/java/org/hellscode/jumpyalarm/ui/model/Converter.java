package org.hellscode.jumpyalarm.ui.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Converter {

    private static String _timeOnlyFormat = "hh:mm aa";

    public static String dateToTimeString(Date newVal) {
        String retVal = "";

        if (newVal != null) {
            retVal = new SimpleDateFormat(_timeOnlyFormat, Locale.US).format(newVal);
        }

        return retVal;
    }
}
