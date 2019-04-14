package com.example.indoorairquality;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class StatsActivity extends AppCompatActivity {
private TextView textView;
private GraphView graph;
private LineGraphSeries<DataPoint> series;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        textView = findViewById(R.id.textView);
        graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3)
        });
        graph.addSeries(series);
        Intent intent = getIntent();
        String name = intent.getStringExtra("parameter");
        if(name.equals("temperature")){
          textView.setText("Temperature History");
        }
        else if(name.equals("nitrogen")){
            textView.setText("Nitrogen History");
        }
        else if(name.equals("humidity")){
            textView.setText("Humidity History");
        }
        else if(name.equals("light")){
            textView.setText("Light History");
        }
        else if(name.equals("noise")){
            textView.setText("Noise History");
        }
        else if(name.equals("carbon")){
            textView.setText("Carbon History");
        }
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
