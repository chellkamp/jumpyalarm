package org.hellscode.jumpyalarm.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmUtil {
    public static Date getNextAlarm(
            Date onOrAfter,
            boolean repeat,
            byte daysOfWeek,
            boolean enabled) {
        Date retVal = null;
        if (onOrAfter != null && enabled) {

            Calendar curCal = Calendar.getInstance();

            Calendar minAlarmTimeCal = Calendar.getInstance();
            minAlarmTimeCal.setTime(onOrAfter);

            if (repeat) {

                Calendar startingPointCal;

                if (curCal.after(minAlarmTimeCal)) {
                    startingPointCal = curCal;
                } else {
                    startingPointCal = minAlarmTimeCal;
                }

                // Combine the latest date with the alarm's time
                Calendar nextAlarmCal = Calendar.getInstance();
                nextAlarmCal.setTime(minAlarmTimeCal.getTime());
                nextAlarmCal.set(Calendar.YEAR, startingPointCal.get(Calendar.YEAR));
                nextAlarmCal.set(Calendar.DAY_OF_YEAR, startingPointCal.get(Calendar.DAY_OF_YEAR));

                int zeroBasedWeekday = startingPointCal.get(Calendar.DAY_OF_WEEK) - 1;
                if(!nextAlarmCal.after(startingPointCal)) {
                    nextAlarmCal.add(Calendar.DAY_OF_MONTH, 1);
                    zeroBasedWeekday = (zeroBasedWeekday + 1) % 7;
                }

                boolean found = false;
                for (int i = 0; !found && i < 7; ++i) {

                    if (((1 << zeroBasedWeekday) & daysOfWeek) != 0) {
                        retVal = nextAlarmCal.getTime();
                        found = true;
                    }
                    nextAlarmCal.add(Calendar.DAY_OF_MONTH, 1);
                    zeroBasedWeekday = (zeroBasedWeekday +1) % 7;
                }

            } else {
                // no repeat
                if (curCal.before(minAlarmTimeCal)) {
                    retVal = onOrAfter;
                }
            }

        }

        return retVal;

    }
}
