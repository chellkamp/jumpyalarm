package org.hellscode.jumpyalarm.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database contracts
 */
public final class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Jumpy";

    private static DatabaseHelper _instance;

    public static void load(Context context) {
        if (_instance == null) {
            _instance = new DatabaseHelper(context);
        }
    }

    public static void recycle() {
        if (_instance != null) {
            _instance.close();
        }
    }

    public static DatabaseHelper getInstance() {
        return _instance;
    }

    /**
     * Constructor
     * @param context context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database needs to be created.
     * @param db database connection
     */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AlarmEntity.createStatement);
    }

    /**
     * Called when the database needs to be upgraded
     * @param db database connection
     * @param oldVersion old version number
     * @param newVersion version to upgrade to
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // first version:  do nothing
    }

}
