package org.hellscode.jumpyalarm.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.provider.BaseColumns;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Handles translation to/from database for Alarm table.
 */
public class AlarmEntity {

    private static class Columns implements BaseColumns {
        static final String Enabled = "Enabled";
        static final String OnOrAfter = "OnOrAfter";
        static final String Repeat = "Repeat";
        static final String DaysOfWeek = "DaysOfWeek";
        static final String Label = "Label";
        static final String Sound = "Sound";
    }

    private static final String _table = "Alarm";
    private static final String _whereClause = String.format("\"%s\" = ?",Columns._ID);

    private long _field__id = -1;
    private boolean _field_Enabled = true;
    private Date _field_OnOrAfter;
    private boolean _field_Repeat;
    private byte _field_DaysOfWeek = 0b0111110;
    private String _field_Label;
    private String _field_Sound;

    /**
     * Constructor
     */
    public AlarmEntity() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        _field_OnOrAfter = c.getTime();
    }

    static final String createStatement =
            "CREATE TABLE \"" + _table + "\" (\n" +
            "  \"" + Columns._ID +  "\" INTEGER PRIMARY KEY,\n" +
            "  \"" + Columns.Enabled + "\" INTEGER NOT NULL,\n" +
            "  \"" + Columns.OnOrAfter + "\" INTEGER NOT NULL,\n" +
            "  \"" + Columns.Repeat + "\" INTEGER NOT NULL,\n" +
            "  \"" + Columns.DaysOfWeek + "\" INTEGER NOT NULL,\n" +
            "  \"" + Columns.Label + "\" TEXT NULL\n" +
            "  \"" + Columns.Sound + "\" TEXT NULL\n" +
            ")";

    public long get_id() { return _field__id; }
    private void set__id(int _id) { _field__id = _id; }

    public boolean getEnabled() { return _field_Enabled; }
    public void setEnabled(boolean enabled) { _field_Enabled = enabled; }

    public Date getOnOrAfter() { return _field_OnOrAfter; }
    public void setOnOrAfter(Date firstAlarm){
        _field_OnOrAfter = firstAlarm;
    }

    public boolean getRepeat() { return _field_Repeat; }
    public void setRepeat(boolean repeat) { _field_Repeat = repeat; }

    public byte getDaysOfWeek() { return _field_DaysOfWeek; }
    public void setDaysOfWeek(byte daysOfWeek) { _field_DaysOfWeek = daysOfWeek; }

    public String getLabel() { return _field_Label; }
    public void setLabel(String label) { _field_Label = label; }

    public String getSound() { return _field_Sound; }
    public void setSound(String sound) { _field_Sound = sound; }

    private long getNonNullDBValue(Date d) {
        long retVal = 0;
        if (d != null) {
            retVal = d.getTime();
        }
        return retVal;
    }

    private Date getDateFromDBValue(long val) {
        return new Date(val);
    }

    private byte getByteFromDBValue(int i) {
        return (byte)i;
    }

    private boolean getBooleanFromDBValue(int i) {
        return (i != 0);
    }

    private ContentValues buildContentValues() {
        ContentValues retVal = new ContentValues();

        //assemble all non-id values
        retVal.put(Columns.Enabled, _field_Enabled);
        retVal.put(Columns.OnOrAfter, getNonNullDBValue(_field_OnOrAfter));
        retVal.put(Columns.Repeat, _field_Repeat);
        retVal.put(Columns.DaysOfWeek, _field_DaysOfWeek);
        retVal.put(Columns.Label, _field_Label);
        retVal.put(Columns.Sound, _field_Sound);
        return retVal;
    }

    /**
     * Persist this single object to the database
     * @param db database connection
     * @throws SQLiteException failed to insert row
     */
    public void persistToDB(@NonNull SQLiteDatabase db)
            throws SQLiteException {

        if (_field__id < 0) {
            // insert
            long rowid = insert(db);

            if (rowid < 0) {
                throw new SQLiteException(
                        String.format(
                                "Failed to insert row into \"%s\" table.",
                                _table
                        )
                );
            }
        } else {
            // update
            int numAffected = update(db);

            if (numAffected < 1) {
                throw new SQLiteException(
                        String.format(
                                "Failed to update row in \"%s\" table for (%s).",
                                _table,
                                _whereClause
                        )
                );
            }

            if (numAffected > 1) {
                throw new SQLiteException(
                        String.format(
                                "Incorrectly updated more than 1 row in table \"%s\" with id column \"%s\"",
                                _table,
                                Columns._ID
                        )
                );
            }
        }

    }

    /**
     * Delete the corresponding entry from the database
     * @param db database connection
     */
    public void deleteFromDB(@NonNull SQLiteDatabase db)
            throws SQLiteException {
        int numAffected = db.delete(_table, _whereClause, new String[]{Long.toString(_field__id)});

        if (numAffected < 1) {
            throw new SQLiteException(
                    String.format(
                            "Failed to delete row in \"%s\" table for (%s).",
                            _table,
                            _whereClause
                    )
            );
        }

        if (numAffected > 1) {
            throw new SQLiteException(
                    String.format(
                            "Incorrectly deleted more than 1 row in table \"%s\" with id column \"%s\"",
                            _table,
                            Columns._ID
                    )
            );
        }

        _field__id = -1; // reset ID field so this object can be reused
    }

    private void loadFromCursor(@NonNull Cursor cursor) {
        _field__id = cursor.getLong(cursor.getColumnIndex(Columns._ID));
        _field_Enabled = getBooleanFromDBValue(cursor.getInt(cursor.getColumnIndex(Columns.Enabled)));
        _field_OnOrAfter = getDateFromDBValue(cursor.getLong(cursor.getColumnIndex(Columns.OnOrAfter)));
        _field_Repeat = getBooleanFromDBValue(cursor.getInt(cursor.getColumnIndex(Columns.Repeat)));
        _field_DaysOfWeek = getByteFromDBValue(cursor.getInt(cursor.getColumnIndex(Columns.DaysOfWeek)));
        _field_Label = cursor.getString(cursor.getColumnIndex(Columns.Label));
        _field_Sound = cursor.getString(cursor.getColumnIndex(Columns.Sound));
    }

    /**
     * Insert this object into a new row.
     * @param db database connection
     */
    private long insert(@NonNull SQLiteDatabase db)
            throws SQLiteException {
        ContentValues v = buildContentValues();

        long rowid = db.insert(_table, null, v);
        _field__id = rowid; // update this object with the new ID
        return rowid;
    }

    private int update(@NonNull SQLiteDatabase db)
            throws SQLiteException {
        ContentValues v = buildContentValues();
        return db.update(_table, v, _whereClause, new String[] {Long.toString(_field__id)});
    }

    /**
     * Load all entries from the database.
     * @param db database connection
     * @return list of objects loaded from the database.  Can be empty, but never null.
     */
    public static @NonNull ArrayList<AlarmEntity> loadAll(SQLiteDatabase db) {
        ArrayList<AlarmEntity> retVal = new ArrayList<>();

        Cursor c = db.query(
                _table,
                null,
                null,
                null,
                null,
                null,
                String.format("\"%s\"", Columns._ID)
        );

        while (c.moveToNext()) {
            AlarmEntity entry = new AlarmEntity();
            entry.loadFromCursor(c);
            retVal.add(entry);
        }

        c.close();

        return retVal;
    }
}
