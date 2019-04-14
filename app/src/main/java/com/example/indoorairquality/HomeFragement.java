package com.example.indoorairquality;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Observable;


public class HomeFragement extends Fragment  {

    View view;
    TextView temperature;
    TextView carbon;
    TextView humidity;
    TextView noise;
    TextView light;
    TextView nitrogen;
    Button temperatureHistory;
    Button carbonHistory;
    Button humidityHistory;
    Button nitrogenHistory;
    Button noiseHistory;
    Button lightHistory;

    private BroadcastReceiver minuteUpdateReceiver;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.home_fragement, container, false);
        temperature=view.findViewById(R.id.temperature);
        carbon=view.findViewById(R.id.carbon);
        humidity=view.findViewById(R.id.humidity);
        noise=view.findViewById(R.id.noise);
        light=view.findViewById(R.id.light);
        nitrogen=view.findViewById(R.id.nitrogen);
        temperatureHistory= view.findViewById(R.id.temperatureHistory);
        carbonHistory= view.findViewById(R.id.carbonHistory);
        humidityHistory= view.findViewById(R.id.humidityHistory);
        noiseHistory= view.findViewById(R.id.noiseHistory);
        nitrogenHistory= view.findViewById(R.id.nitrogenHistory);
        lightHistory= view.findViewById(R.id.lightHistory);
        Bundle bundle = this.getArguments();
        if(bundle!=null){
            temperature.setText(bundle.getString("temperature")+" °C");
            temperature.setTextColor(temperatureColor((Double.parseDouble(bundle.getString("temperature")))));
            carbon.setText(bundle.getString("carbon")+" ppm");
            carbon.setTextColor(carbonColor(Double.parseDouble(bundle.getString("carbon"))));
            humidity.setText(bundle.getString("humidity")+" %");
            humidity.setTextColor(humidityColor(Double.parseDouble(bundle.getString("humidity"))));
            noise.setText(bundle.getString("noise")+" dB");
            noise.setTextColor(noiseColor(Double.parseDouble(bundle.getString("noise"))));
            light.setText(bundle.getString("light")+" lux");
            nitrogen.setText(bundle.getString("nitrogen")+" ppb");
            nitrogen.setTextColor(nitrogenColor(Double.parseDouble(bundle.getString("nitrogen"))));
        }
        temperatureHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do something Like starting an activity
                //Toast.makeText(getContext(), "You have clicked P1", Toast.LENGTH_LONG).show();
               Intent intent = new Intent(getContext(), StatsActivity.class);
               intent.putExtra("parameter","temperature");
               startActivity(intent);
            }
        });
        nitrogenHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do something Like starting an activity
                //Toast.makeText(getContext(), "You have clicked P1", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), StatsActivity.class);
                intent.putExtra("parameter","nitrogen");
                startActivity(intent);
            }
        });
        humidityHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do something Like starting an activity
                //Toast.makeText(getContext(), "You have clicked P1", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), StatsActivity.class);
                intent.putExtra("parameter","humidity");
                startActivity(intent);
            }
        });
        carbonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do something Like starting an activity
                //Toast.makeText(getContext(), "You have clicked P1", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), StatsActivity.class);
                intent.putExtra("parameter","carbon");
                startActivity(intent);
            }
        });
        noiseHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do something Like starting an activity
                //Toast.makeText(getContext(), "You have clicked P1", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), StatsActivity.class);
                intent.putExtra("parameter","noise");
                startActivity(intent);
            }
        });
        lightHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do something Like starting an activity
                //Toast.makeText(getContext(), "You have clicked P1", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), StatsActivity.class);
                intent.putExtra("parameter","light");
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        startMinuteUpdater();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unregisterReceiver(minuteUpdateReceiver);

    }

    public void startMinuteUpdater() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        minuteUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                MeasurementDAO measurementDAO = new MeasurementDAO(getContext());
                Measurement measurement= measurementDAO.getLastMeasurement();
                double temp=(double)Math.round(measurement.get_temp());
                temperature.setText(""+temp+" °C");
                temperature.setTextColor(temperatureColor(temp));
                double car =(double)Math.round(measurement.get_CO());
                carbon.setText(""+car+" ppm");
                carbon.setTextColor(carbonColor(car));
                double hum=(double)Math.round(measurement.get_humidity());
                humidity.setText(""+hum+" %");
                humidity.setTextColor(humidityColor(hum));
                double nois= (double)Math.round(measurement.get_noise());
                noise.setText(""+nois+" dB");
                noise.setTextColor(noiseColor(nois));
                double lig=(double)Math.round(measurement.get_light());
                light.setText(""+lig+" lux");
                double nit=(double)Math.round(measurement.get_NO2());
                nitrogen.setText(""+nit+" ppb");
                nitrogen.setTextColor(nitrogenColor(nit));
            }
        };
        getActivity().registerReceiver(minuteUpdateReceiver, intentFilter);
    }

    private int temperatureColor(double value){
        //if(value>=21.0 && value<=26.0){
        if(value>=19.0 && value<=26.0){
            return getResources().getColor(R.color.good_color);
        }
        else if(value>=10.0 && value<19.0 ||value>=27.0 && value<40.0){
          return getResources().getColor(R.color.fair_color);
        }
        else{
            return getResources().getColor(R.color.bad_color);
        }

    }
    private int carbonColor(double value){
        if(value>=400.0 && value<=800.0){
            return getResources().getColor(R.color.good_color);
        }
        else if(value>=1500.0 && value<=2500.0 ){
            return getResources().getColor(R.color.fair_color);
        }
        else{
            return getResources().getColor(R.color.bad_color);
        }

    }
    private int humidityColor(double value){
        if(value>=30.0 && value<=50.0){
            return getResources().getColor(R.color.good_color);
        }
        else if(value>=10.0 && value<30.0 ||value>=50.0 && value<=100.0){
            return getResources().getColor(R.color.fair_color);
        }
        else{
            return getResources().getColor(R.color.bad_color);
        }

    }
    private int noiseColor(double value){
        if(value<=75.0){
            return getResources().getColor(R.color.good_color);
        }
        else if(value>75.0 && value<=85.0){
            return getResources().getColor(R.color.fair_color);
        }
        else{
            return getResources().getColor(R.color.bad_color);
        }

    }
    private int lightColor(double value){
        if(value>=21.0 && value<=26.0){
            return getResources().getColor(R.color.good_color);
        }
        else if(value>=10.0 && value<21.0 ||value>=27.0 && value<40.0){
            return getResources().getColor(R.color.fair_color);
        }
        else{
            return getResources().getColor(R.color.bad_color);
        }

    }
    private int nitrogenColor(double value){
        if(value>=0.0 && value<=100.0){
            return getResources().getColor(R.color.good_color);
        }
        else if(value>=100.0 && value<250.0){
            return getResources().getColor(R.color.fair_color);
        }
        else{
            return getResources().getColor(R.color.bad_color);
        }

    }
}