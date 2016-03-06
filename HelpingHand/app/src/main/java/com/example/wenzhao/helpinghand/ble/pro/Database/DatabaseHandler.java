package com.example.wenzhao.helpinghand.ble.pro.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.Double;import java.lang.Long;import java.lang.Override;import java.lang.String;import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/3/3.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static DatabaseHandler databasehandler = null;
    private static Context mContext = null;
    private static String CURRENT_DB_PATH = null;
    private static String BACKUP_DB_PATH_DIR = null;

    public static final String TABLE_ENTRIES = "entry";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ACTIVITY = "activity";
    public static final String COLUMN_RATIO = "ratio";
    public static final String COLUMN_TIME= "time";

    private static final String DATABASE_NAME = "current.db";
    private static final int DATABASE_VERSION = 2;

    /*private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_ENTRIES
            + "("
            + COLUMN_ID        + " integer primary key autoincrement,"
            + COLUMN_ACTIVITY + " text not null,"
            + COLUMN_RATIO     + " text not null"
            + ");";
*/
    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        databasehandler = this;
    }

    public static DatabaseHandler getHandler(){
        return databasehandler;
    }

    public static DatabaseHandler initHandler(Context context) {
        if (databasehandler == null) {
            databasehandler = new DatabaseHandler(context);
        }
        BACKUP_DB_PATH_DIR = "/data/data/" + mContext.getPackageName() + "/databases/backup/";
        SQLiteDatabase db = databasehandler.getReadableDatabase();
        CURRENT_DB_PATH = db.getPath();
        db.close();

        return  databasehandler;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String DATABASE_CREATE = "CREATE TABLE "
                + TABLE_ENTRIES
                + "("
                + COLUMN_ID        + " integer primary key autoincrement,"
                + COLUMN_ACTIVITY + " text not null, "
                + COLUMN_RATIO     + " text not null, "
                + COLUMN_TIME      + " text not null"
                + ");";

        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHandler.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);
        onCreate(db);
    }

    private void deleteDatabase () {
        mContext.deleteDatabase(DATABASE_NAME);
        databasehandler = null;
        DatabaseHandler.initHandler(mContext);
    }

    public static String getDBPathDir () {
        return BACKUP_DB_PATH_DIR;
    }

    public void exportDatabase(String name) throws IOException {
        // Closes all database connections to commit to mem
        databasehandler.close();
        // Determines paths
        String outFileName = BACKUP_DB_PATH_DIR + name + ".db";

        // Checks if destination folder exists and create if not
        File createOutFile = new File(BACKUP_DB_PATH_DIR);
        if (!createOutFile.exists()){
            createOutFile.mkdir();
        }

        File fromDB = new File(CURRENT_DB_PATH);
        File toDB = new File(outFileName);
        // Copy the database to the new location
        copyFile(new FileInputStream(fromDB), new FileOutputStream(toDB));
        Log.d("export Database", "copy succeeded");
        // Delete Existing Database
        deleteDatabase();
    }

    public void importDatabase (String name) throws IOException {
        // Closes all database connections to commit to mem
        databasehandler.close();
        // Determines paths
        String inFileName = BACKUP_DB_PATH_DIR + name;

        File fromDB = new File(inFileName);
        File toDB = new File(CURRENT_DB_PATH);
        // Copy the database to the new location
        copyFile(new FileInputStream(fromDB), new FileOutputStream(toDB));
        Log.d("Import Database", "succeeded");
    }
    private static void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        try {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
        } finally {
            try {
                if (fromChannel != null) {
                    fromChannel.close();
                }
            } finally {
                if (toChannel != null) {
                    toChannel.close();
                }
            }
        }
    }

    // Adding new value
    public void addValue(ChildInfo entry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put (COLUMN_ACTIVITY, entry.getActivity());
        values.put(COLUMN_RATIO,entry.getFinalRatio());
        values.put(COLUMN_TIME,entry.getFinishtime());

        // Inserting into database
        db.insert(TABLE_ENTRIES, null, values);
        db.close();
    }

    public ChildInfo getValue( int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Uses a cursor to query from the database.
        // Provides the strings we want from the query and the query parameters
        Cursor cursor = db.query(TABLE_ENTRIES, new String[]{
                COLUMN_ID,
                COLUMN_ACTIVITY,
                COLUMN_RATIO,
                COLUMN_TIME

        }
                , COLUMN_ID + "=?", new String[]{
                String.valueOf(id)
        }
                , null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ChildInfo entry = new ChildInfo(
                Long.parseLong(cursor.getString(0)),    // ID
                cursor.getString(1),                    //Activity
                Double.parseDouble(cursor.getString(2)), //Ratio
                Double.parseDouble(cursor.getString(3)) // finish time
                );

        db.close();
        return entry;
    }

    public List<ChildInfo> getActivityValues(String activity) {
        List<ChildInfo> dataEntryList = new ArrayList<ChildInfo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ENTRIES;

        SQLiteDatabase db = this.getWritableDatabase();
        // Uses a cursor to query from the database.
        // Provides the strings we want from the query and the query parameters
        Cursor cursor = db.query(TABLE_ENTRIES, new String[]{
                COLUMN_ID,
                COLUMN_ACTIVITY,
                COLUMN_RATIO,
                COLUMN_TIME

        }
                , COLUMN_ACTIVITY + "=?" , new String[]{
                activity
        }
                ,null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChildInfo entry = new ChildInfo(
                        Long.parseLong(cursor.getString(0)),    // ID
                        cursor.getString(1),                    // Activity
                        Double.parseDouble(cursor.getString(2)), // final ratio
                        Double.parseDouble(cursor.getString(3))  // finish time
                );
                // Adding contact to list
                dataEntryList.add(entry);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dataEntryList;
    }



    public void deleteValue(ChildInfo entry) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_ENTRIES, COLUMN_ID + " = ?",
                new String[] { String.valueOf(entry.getId()) });
        db.close();
    }

    public List<ChildInfo> getAllValues() {
        List<ChildInfo> dataEntryList = new ArrayList<ChildInfo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ENTRIES;

        SQLiteDatabase db = this.getWritableDatabase();
        // Uses a cursor to query from the database.
        // Provides the strings we want from the query and the query parameters
        Cursor cursor = db.query(TABLE_ENTRIES, new String[] {
                COLUMN_ID,
                COLUMN_ACTIVITY,
                COLUMN_RATIO,
                COLUMN_TIME
        }
                ,null, null, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChildInfo entry = new ChildInfo(
                        Long.parseLong(cursor.getString(0)),    // ID
                        cursor.getString(1),                    // Activity
                        Double.parseDouble(cursor.getString(2)), // final ratio
                        Double.parseDouble(cursor.getString(3)) //finish time
                );
                // Adding contact to list
                dataEntryList.add(entry);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dataEntryList;
    }

    public int getValuesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ENTRIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}



