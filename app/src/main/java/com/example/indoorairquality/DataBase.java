package com.example.indoorairquality;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
public class DataBase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "IAQdb";
    public static final String KEY_ID="id";
    public static final String MEASUREMENT_TABLE_NAME ="measurements";
    public static final String KEY_TEMPERATURE = "temperature";
    public static final String KEY_CO = "co";
    public static final String KEY_NO2 = "no2";
    public static final String KEY_HUM = "humidity";
    public static final String KEY_DATE = "date";
    public static final String MEASUREMENT_TABLE_CREATE =
            "CREATE TABLE " + MEASUREMENT_TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_TEMPERATURE + " REAL, " + KEY_CO+" REAL, " + KEY_NO2 +" REAL, " +
                    KEY_HUM +" REAL);";
    public static final String METIER_TABLE_DROP = "DROP TABLE IF EXISTS " + MEASUREMENT_TABLE_NAME + ";";

    public DataBase(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(MEASUREMENT_TABLE_CREATE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(METIER_TABLE_DROP);
        onCreate(db);
    }
}
