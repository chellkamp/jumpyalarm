package org.hellscode.jumpyalarm;

import android.app.AlertDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hellscode.jumpyalarm.data.AlarmEntity;
import org.hellscode.jumpyalarm.data.DatabaseHelper;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("org.hellscode.jumpyalarm", appContext.getPackageName());
    }

    @Test
    public void initDB() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        DatabaseHelper dbHelper = new DatabaseHelper(appContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("DELETE FROM \"Alarm\"");

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 30);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        for (int i = 0; i < 3; ++i) {
            AlarmEntity entity = new AlarmEntity();
            entity.setFirstAlarm(c.getTime());
            entity.setRepeat(true);
            entity.setDaysOfWeek((byte)0b10); // Monday only
            entity.setLabel(String.format("test alarm %d", i+1));

            assertEquals(-1L, entity.get_id());

            entity.persistToDB(db);

            assertEquals(i+1, entity.get_id());
            c.add(Calendar.HOUR_OF_DAY, 1);
        }

    }

    @Test
    public void readDBEntries() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        DatabaseHelper dbHelper = new DatabaseHelper(appContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<AlarmEntity> entities = AlarmEntity.loadAll(db);

        for(AlarmEntity e : entities) {
            System.out.println();
            System.out.println(e.getLabel());
            System.out.println(e.getFirstAlarm());
            System.out.println(e.getRepeat());
            System.out.println(e.getDaysOfWeek());
        }

    }
}
