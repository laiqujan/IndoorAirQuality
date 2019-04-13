package com.example.indoorairquality;

import android.util.Log;

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

    public Measurement(){}
    public Measurement(double temp, double co,double no2, double hum, double noise, double light){
        _id=_id;
        _temp=temp;
        _CO=co;
        _NO2=no2;
        _humidity=hum;
        _noise=noise;
        _light=light;

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

}
