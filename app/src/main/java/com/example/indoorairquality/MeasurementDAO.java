package com.example.indoorairquality;

import android.content.ContentValues;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.database.Cursor;
import java.util.ArrayList;
public class MeasurementDAO extends DAOBase {
    //This class is used to handle Database with specific operation
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "IAQdb";
    public static final String KEY_ID="id";
    public static final String MEASUREMENT_TABLE_NAME ="measurements";
    public static final String KEY_TEMPERATURE = "temperature";
    public static final String KEY_CO = "co";
    public static final String KEY_NO2 = "no2";
    public static final String KEY_HUM = "humidity";
    public static final String KEY_NOISE= "noise";
    public static final String KEY_LIGHT = "light";
    public static final String KEY_DATE = "date";
    public static final String MEASUREMENT_TABLE_CREATE =
            "CREATE TABLE " + MEASUREMENT_TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_TEMPERATURE + " REAL, " + KEY_CO+" REAL, " + KEY_NO2 +" REAL, " +
                    KEY_NOISE+" REAL, " + KEY_LIGHT+" REAL, " +KEY_HUM +" REAL);";
    public static final String METIER_TABLE_DROP = "DROP TABLE IF EXISTS " + MEASUREMENT_TABLE_NAME + ";";




    public MeasurementDAO(Context pContext){
        super(pContext);
    }
    public void addMeasurement (Measurement pMeasurement) {
        mDb = mHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TEMPERATURE, pMeasurement.get_temp());
        values.put(KEY_CO, pMeasurement.get_CO());
        values.put(KEY_NO2, pMeasurement.get_NO2());
        values.put(KEY_HUM, pMeasurement.get_humidity());
        values.put(KEY_NOISE, pMeasurement.get_noise());
        values.put(KEY_LIGHT, pMeasurement.get_light());
        long check=mDb.insert(MEASUREMENT_TABLE_NAME,null,values);
        Log.d("Line dataBase", String.valueOf(check));
        mDb.close();
    }
    public int getNbline(){
        mDb = mHandler.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + MEASUREMENT_TABLE_NAME ;//+ " ORDER BY id DESC LIMIT 10";
        Cursor cursor = mDb.rawQuery(selectQuery, null);
        return cursor.getCount();
    }
    public Measurement getLastMeasurement(){
        mDb = mHandler.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + MEASUREMENT_TABLE_NAME + " ORDER BY id DESC LIMIT 1";
        Cursor cursor = mDb.rawQuery(selectQuery, null);
        if (cursor != null)
            cursor.moveToLast();
        Measurement temp =new Measurement();
        // this is quick fix for bug which crashes if db is empty
        if (cursor.getCount() == 0) {
            Log.d("cursor", "cursor is empty");
            temp.set_id(0);
            temp.set_temp(0);
            temp.set_CO(0);
            temp.set_NO2(0);
            temp.set_humidity(0);
            temp.set_noise(0);
            temp.set_light(0);
        }
        else {
            temp.set_id(cursor.getInt(0));
            temp.set_temp(cursor.getDouble(1));
            temp.set_CO(cursor.getDouble(2));
            temp.set_NO2(cursor.getDouble(3));
            temp.set_humidity(cursor.getDouble(4));
            temp.set_noise(cursor.getDouble(5));
            temp.set_light(cursor.getDouble(6));
        }
        Log.d("ID", Long.toString(temp.get_id()));
        Log.d("TEMPERATURE", Double.toString(temp.get_temp()));
        Log.d("CO", Double.toString(temp.get_CO()));
        Log.d("NO2", Double.toString(temp.get_NO2()));
        Log.d("HUMIDITY", Double.toString(temp.get_humidity()));
        Log.d("NOISE", Double.toString(temp.get_noise()));
        Log.d("LIGHT", Double.toString(temp.get_light()));
        cursor.close();
        mDb.close();
        return temp;
    }

    public ArrayList<Measurement> getAllData(){
        ArrayList<Measurement> list = new ArrayList<Measurement>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + MEASUREMENT_TABLE_NAME;

        mDb = mHandler.getReadableDatabase();
        try {

            Cursor cursor = mDb.rawQuery(selectQuery, null);
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        Measurement obj = new Measurement();

                        obj.set_id(cursor.getInt(0));
                        obj.set_temp(cursor.getDouble(1));
                        obj.set_CO(cursor.getDouble(2));
                        obj.set_NO2(cursor.getDouble(3));
                        obj.set_humidity(cursor.getDouble(4));
                        obj.set_noise(cursor.getDouble(5));
                        obj.set_light(cursor.getDouble(6));
                        list.add(obj);
                    } while (cursor.moveToNext());
                }

            } finally {
                try { cursor.close(); } catch (Exception ignore) {}
            }

        } finally {
            try { mDb.close(); } catch (Exception ignore) {}
        }

        return list;
    }

}
