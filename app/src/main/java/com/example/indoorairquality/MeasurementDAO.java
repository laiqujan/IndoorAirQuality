package com.example.indoorairquality;

import android.content.ContentValues;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.Date;

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
    public static final String KEY_DateLong = "longDate";//format that can be handle by SQL, e.g we can sort the value
    public static final String KEY_DateString= "stringDate";//format we want to store but SQL can't handle it
    public static final String MEASUREMENT_TABLE_CREATE =
            "CREATE TABLE " + MEASUREMENT_TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_TEMPERATURE + " REAL, " + KEY_CO+" REAL, " + KEY_NO2 +" REAL, " +
                    KEY_NOISE+" REAL, " + KEY_LIGHT+" REAL, " +KEY_HUM +" REAL,"+ KEY_DateString +" TEXT," + KEY_DateLong +" INTEGER);";
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
        values.put(KEY_DateString,pMeasurement.get_date_string());
        values.put(KEY_DateLong,pMeasurement.get_date_long());
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
            temp.set_date_string("1970-00-00T00:00:00Z");//calendarDate set at the same time
            temp.set_date_long(0);

        }
        else {
            temp.set_id(cursor.getInt(0));
            temp.set_temp(cursor.getDouble(1));
            temp.set_CO(cursor.getDouble(2));
            temp.set_NO2(cursor.getDouble(3));
            temp.set_humidity(cursor.getDouble(4));
            temp.set_noise(cursor.getDouble(5));
            temp.set_light(cursor.getDouble(6));
            temp.set_date_string(cursor.getString(7));//calendarDate set at the same time
            temp.set_date_long(cursor.getLong(8));
        }
        Log.d("ID", Long.toString(temp.get_id()));
        Log.d("TEMPERATURE", Double.toString(temp.get_temp()));
        Log.d("CO", Double.toString(temp.get_CO()));
        Log.d("NO2", Double.toString(temp.get_NO2()));
        Log.d("HUMIDITY", Double.toString(temp.get_humidity()));
        Log.d("NOISE", Double.toString(temp.get_noise()));
        Log.d("LIGHT", Double.toString(temp.get_light()));
        Log.d("DATEString", temp.get_date_string());
        Log.d("DateLong", Long.toString(temp.get_date_long()));
        cursor.close();
        mDb.close();
        return temp;
    }

    //Function that return a list of Measurement, all the measurement in data base
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
                        obj.set_date_string(cursor.getString(7));//calendarDate set at the same time
                        obj.set_date_long(cursor.getLong(8));
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

    //function that return a list of measurement with all data and date
    public  ArrayList<Measurement> getWeekData(){
        //Get the last data to know the its date
        Measurement last = getLastMeasurement();
        long limitDate= last.get_date_long()-7000000;//date corresponding at 1 week before the last one
        //we subtract 7000000 because it means subtract 7 days to the date in long format
        //this date will be the limit in the query to get one week data

        ArrayList<Measurement> list = new ArrayList<Measurement>();

        // Select 1 week query Query
        String selectQuery = "SELECT  * FROM " + MEASUREMENT_TABLE_NAME + " WHERE longdate > ?";

        mDb = mHandler.getReadableDatabase();
        try {

            Cursor cursor = mDb.rawQuery(selectQuery, new String[]{Long.toString(limitDate)});
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
                        obj.set_date_string(cursor.getString(7));//calendarDate set at the same time
                        obj.set_date_long(cursor.getLong(8));
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

    //function that return  list of CO data of the previous week ||Warning : can't know the date of each data
    public ArrayList<Double> getWeekCO(){
        ArrayList<Measurement> list = new ArrayList<Measurement>();
        ArrayList<Double> carbon_List = new ArrayList<Double>();
        list = getWeekData();
        //course of list to get all carbon data
        for(int i=0; i<list.size(); i++){
            carbon_List.add(list.get(i).get_CO());
        }
        return carbon_List;
    }
    //function that return  list of NO2 data of the previous week ||Warning : can't know the date of each data
    public ArrayList<Double> getWeekNO2(){
        ArrayList<Measurement> list = new ArrayList<Measurement>();
        ArrayList<Double> NO_List = new ArrayList<java.lang.Double>();
        list = getWeekData();
        //course of list to get all carbon data
        for(int i=0; i<list.size(); i++){
            NO_List.add(list.get(i).get_NO2());
        }
        return NO_List;
    }
    public ArrayList<Double> getWeekHumidity(){
        ArrayList<Measurement> list = new ArrayList<Measurement>();
        ArrayList<Double> hum_List = new ArrayList<Double>();
        list = getWeekData();
        //course of list to get all carbon data
        for(int i=0; i<list.size(); i++){
            hum_List.add(list.get(i).get_humidity());
        }
        return hum_List;
    }
    public ArrayList<Double> getWeekNoise(){
        ArrayList<Measurement> list = new ArrayList<Measurement>();
        ArrayList<Double> noise_List = new ArrayList<Double>();
        list = getWeekData();
        //course of list to get all carbon data
        for(int i=0; i<list.size(); i++){
            noise_List.add(list.get(i).get_noise());
        }
        return noise_List;
    }
    public ArrayList<Double> getWeekLight(){
        ArrayList<Measurement> list = new ArrayList<Measurement>();
        ArrayList<Double> light_List = new ArrayList<Double>();
        list = getWeekData();
        //course of list to get all carbon data
        for(int i=0; i<list.size(); i++){
            light_List.add(list.get(i).get_light());
        }
        return light_List;
    }
    public ArrayList<Double> getWeekTemperature(){
        ArrayList<Measurement> list = new ArrayList<Measurement>();
        ArrayList<Double> temp_List = new ArrayList<Double>();
        list = getWeekData();
        //course of list to get all carbon data
        for(int i=0; i<list.size(); i++){
            temp_List.add(list.get(i).get_temp());
        }
        return temp_List;
    }

}
