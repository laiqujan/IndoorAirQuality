package com.example.indoorairquality;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {
private TextView textView;
private GraphView graph;
private MeasurementDAO measurementDAO;
private ArrayList<Measurement> measurementArrayList;
private BarGraphSeries<DataPoint> series;
//private DataPoint[] dataPoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        //textView = findViewById(R.id.textView);
        measurementDAO=new MeasurementDAO(getApplicationContext());
        measurementArrayList=measurementDAO.getWeekData();
        //dataPoint=new DataPoint[measurementArrayList.size()];
        graph = (GraphView) findViewById(R.id.graph);
        Intent intent = getIntent();
        String name = intent.getStringExtra("parameter");
        series=new BarGraphSeries<>();
        if(name.equals("temperature")){
            //textView.setText("Temperature History");
            graph.setTitle("Weekly Temperature History ");
            for(int i=0; i<measurementArrayList.size(); i++){
                series.appendData(new DataPoint(measurementArrayList.get(i).get_date().getTime(),
                        measurementArrayList.get(i).get_temp()),true,measurementArrayList.size());
            }
        }
        else if(name.equals("nitrogen")){
           // textView.setText("Nitrogen History");
            graph.setTitle("Weekly Nitrogen History ");
            for(int i=0; i<measurementArrayList.size(); i++){
                series.appendData(new DataPoint(measurementArrayList.get(i).get_date().getTime(),
                        measurementArrayList.get(i).get_NO2()),true,measurementArrayList.size());
            }
        }
        else if(name.equals("humidity")){
            //textView.setText("Humidity History");
            graph.setTitle("Weekly Humidity History ");
            for(int i=0; i<measurementArrayList.size(); i++){
                series.appendData(new DataPoint(measurementArrayList.get(i).get_date().getTime(),
                        measurementArrayList.get(i).get_humidity()),true,measurementArrayList.size());
            }
        }
        else if(name.equals("light")){
            //textView.setText("Light History");
            graph.setTitle("Weekly Light History ");
            for(int i=0; i<measurementArrayList.size(); i++){
                series.appendData(new DataPoint(measurementArrayList.get(i).get_date().getTime(),
                        measurementArrayList.get(i).get_light()),true,measurementArrayList.size());
            }
        }
        else if(name.equals("noise")){
            //textView.setText("Noise History");
            graph.setTitle("Weekly Noise History ");
            for(int i=0; i<measurementArrayList.size(); i++) {
                series.appendData(new DataPoint(measurementArrayList.get(i).get_date().getTime(),
                        measurementArrayList.get(i).get_noise()),true,measurementArrayList.size());
            }
        }
        else if(name.equals("carbon")){
            //textView.setText("Carbon History");
            graph.setTitle("Weekly Carbon History "+measurementArrayList.get(0).get_date().getTimeInMillis());
            for(int i=0; i<measurementArrayList.size(); i++){
                series.appendData(new DataPoint(measurementArrayList.get(i).get_date().getTime(),
                        measurementArrayList.get(i).get_CO()),true,measurementArrayList.size());
            }
        }
        series.setSpacing(20);
        graph.addSeries(series);
        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(7); // only 7 because of the space
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Date/Time");
        //graph.getGridLabelRenderer().setVerticalAxisTitle("Data");
        graph.getViewport().setScrollable(true);
        graph.setTitleColor(getResources().getColor(R.color.white));
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getResources().getColor(R.color.white));
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.white));
       // graph.getGridLabelRenderer().setHumanRounding(false);
        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        graph.getViewport().setScrollableY(true); // enables vertical scrolling
        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling


        // On close icon click finish activity
        findViewById(R.id.close_activity).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        finish();

                    }
                });

    }
}
