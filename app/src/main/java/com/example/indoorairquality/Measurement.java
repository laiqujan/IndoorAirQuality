package com.example.indoorairquality;

import android.os.Build;
import android.util.Log;

import java.util.Calendar;
import java.util.Calendar.Builder;
import java.util.Observable;

public class Measurement {
    private Measurement measurement;
    private long _id;
    private double _temp;// Celsius
    private double _CO;//ppm
    private double _NO2;//ppm
    private double _humidity;//%
    private double _noise;//dB (decibel)
    private double _light;//lux (1 lux = 1 lumen/m²)
    private String _date_string;
    private long _date_long;//format to be handle in data Base because SQLite doesn't provide Date or DateTime management
    private Calendar _date;//global format to be used for graph


    public Measurement(){}
    //Constructor used with data coming from JSONObject
    public Measurement(double temp, double co,double no2, double hum, double noise, double light, String date){
        _id=_id;
        _temp=temp;
        _CO=co;
        _NO2=no2;
        _humidity=hum;
        _noise=noise;
        _light=light;
        _date_string=date;
        _date=Calendar.getInstance();
        //to switch the string date into a long we need to delete some char
        //date in string looks like 2019-04-11T12:47:37Z so we need to delete - T : and Z
        String tmp = date;
        tmp=tmp.replace("-","");
        tmp=tmp.replace("T","");
        tmp=tmp.replace(":","");
        tmp=tmp.replace("Z","");//now the string looks like 20190411124737
        _date_long=Long.parseLong(tmp);
        // to create the calendar we need to set each parameter e.g year month...
        if(Build.VERSION.SDK_INT >= 26){//Calendar.Builder only with API>=26
            Calendar.Builder cBuilder = new Calendar.Builder().setCalendarType("gregorian");
            cBuilder.setDate(Integer.parseInt(tmp.substring(0,3)),Integer.parseInt(tmp.substring(4,5)),Integer.parseInt(tmp.substring(6,7)));
            cBuilder.setTimeOfDay(Integer.parseInt(tmp.substring(8,9)),Integer.parseInt(tmp.substring(10,11)),Integer.parseInt(tmp.substring(12,13)));
            _date=cBuilder.build();
        }
        else {
            _date.set(Integer.parseInt(tmp.substring(0,3)),Integer.parseInt(tmp.substring(4,5)),Integer.parseInt(tmp.substring(6,7)),
                    Integer.parseInt(tmp.substring(8,9)),Integer.parseInt(tmp.substring(10,11)),Integer.parseInt(tmp.substring(12,13)));
        }

    }
    public long get_id(){
        return _id;
    }

    public double get_CO() {
        return _CO;
    }

    public double get_humidity() {
        return _humidity;
    }

    public double get_NO2() {
        return _NO2;
    }

    public double get_temp() {
        return _temp;
    }

    public double get_noise() {
        return _noise;
    }

    public double get_light() {
        return _light;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public void set_CO(double _CO) {
        this._CO = _CO;
    }

    public void set_humidity(double _humidity) {
        this._humidity = _humidity;
    }
    public void set_NO2(double _NO2) {
        this._NO2 = _NO2;
    }

    public void set_temp(double _temp) {
        this._temp = _temp;
    }

    public void set_noise(double _noise) {
        this._noise = _noise;
    }

    public void set_light(double _light) {
        this._light = _light;
    }

    public String get_date_string() {
        return _date_string;
    }

    public long get_date_long() {
        return _date_long;
    }
    //the format need to be exactly like : 2019-04-11T12:47:37Z
    public void set_date_string(String date) {
        this._date_string = date;
        _date=Calendar.getInstance();
        // we set the long date format at the same time to avoid user to do it
        String tmp;
        tmp = date;
        tmp=tmp.replace("-","");
        tmp=tmp.replace("T","");
        tmp=tmp.replace(":","");
        tmp=tmp.replace("Z","");//now the string looks like 20190411124737
        Log.d("string", tmp);
        _date_long=Long.parseLong(tmp);
        // to create the calendar we need to set each parameter e.g year month...
        if(Build.VERSION.SDK_INT >= 26){//Calendar.Builder only with API>=26
            Calendar.Builder cBuilder = new Calendar.Builder().setCalendarType("gregorian");
            cBuilder.setDate(Integer.parseInt(tmp.substring(0,3)),Integer.parseInt(tmp.substring(4,5)),Integer.parseInt(tmp.substring(6,7)));
            cBuilder.setTimeOfDay(Integer.parseInt(tmp.substring(8,9)),Integer.parseInt(tmp.substring(10,11)),Integer.parseInt(tmp.substring(12,13)));
            _date=cBuilder.build();
        }
        else {
            _date.set(Integer.parseInt(tmp.substring(0,3)),Integer.parseInt(tmp.substring(4,5)),Integer.parseInt(tmp.substring(6,7)),
                    Integer.parseInt(tmp.substring(8,9)),Integer.parseInt(tmp.substring(10,11)),Integer.parseInt(tmp.substring(12,13)));
        }

    }

    public void set_date_long(long _date_long) {
        this._date_long = _date_long;
    }

    public void set_date(Calendar _date) {
        this._date = _date;
    }

    public Calendar get_date() {
        return _date;
    }
}
