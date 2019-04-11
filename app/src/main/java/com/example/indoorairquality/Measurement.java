package com.example.indoorairquality;

public class Measurement {
    private long _id;
    private double _temp;
    private double _CO;
    private double _NO2;
    private double _humidity;

    public Measurement(){}
    public Measurement(double temp, double co,double no2, double hum){
        _id=_id;
        _temp=temp;
        _CO=co;
        _NO2=no2;
        _humidity=hum;
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
}
