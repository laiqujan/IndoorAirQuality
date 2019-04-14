package com.example.indoorairquality;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
//import for data fetching
import android.util.Log;
import org.json.JSONArray;
import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import android.content.Intent;
import java.net.URL;
import android.os.AsyncTask;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //Variable for API Data fetch
    public static WeatherTask weatherTask = new WeatherTask();
    private static MeasurementDAO dbDAO;
    private static Context mainAppContext;
    private Measurement measurement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        dbDAO=new MeasurementDAO(this);//DAO for measurement database
        measurement=dbDAO.getLastMeasurement();
        startService(new Intent(this, DataService.class));//Start the service to fetch data from API
        mainAppContext = getApplication();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            Fragment newFragment = new HomeFragement();
            Bundle data=new Bundle();
            data.putString("temperature",""+(double)Math.round(measurement.get_temp()));
            data.putString("carbon",""+(double)Math.round(measurement.get_CO()));
            data.putString("humidity",""+(double)Math.round(measurement.get_humidity()));
            data.putString("noise",""+(double)Math.round(measurement.get_noise()));
            data.putString("light",""+(double)Math.round(measurement.get_light()));
            data.putString("nitrogen",""+(double)Math.round(measurement.get_NO2()));
            newFragment.setArguments(data);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.content_frame, newFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
        /*
        <android.support.design.widget.FloatingActionButton
        android:id="@+id/fab"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|end"
        android:layout_margin="@dimen/fab_margin"
        app:srcCompat="@android:drawable/ic_dialog_email" />
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
       // MeasurementDAO measurementDAO = new MeasurementDAO(mainAppContext);
       // Measurement measurement= measurementDAO.getLastMeasurement();
        Fragment fragment = null;
        Bundle data = new Bundle();
        if (id == R.id.home) {
            data.putString("temperature",""+(double)Math.round(measurement.get_temp()));
            data.putString("carbon",""+(double)Math.round(measurement.get_CO()));
            data.putString("humidity",""+(double)Math.round(measurement.get_humidity()));
            data.putString("noise",""+(double)Math.round(measurement.get_noise()));
            data.putString("light",""+(double)Math.round(measurement.get_light()));
            data.putString("nitrogen",""+(double)Math.round(measurement.get_NO2()));
            fragment = new HomeFragement();
            fragment.setArguments(data);
        }
        else if (id == R.id.add_sensor) {
            fragment= new AddSensor();
        } else if (id == R.id.sensors) {
            fragment= new SensorFragment();
        } else if (id == R.id.units) {
            fragment= new UnitsFragment();
        }  else if (id == R.id.profile) {
            fragment= new ProfileFragment();
        } else if (id == R.id.logout) {
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }
        else if (id == R.id.policy) {
            // re-direct to we web page
        }
        if (fragment == null) {
            data.putString("temperature",""+(double)Math.round(measurement.get_temp()));
            data.putString("carbon",""+(double)Math.round(measurement.get_CO()));
            data.putString("humidity",""+(double)Math.round(measurement.get_humidity()));
            data.putString("noise",""+(double)Math.round(measurement.get_noise()));
            data.putString("light",""+(double)Math.round(measurement.get_light()));
            data.putString("nitrogen",""+(double)Math.round(measurement.get_NO2()));
            fragment = new HomeFragement();
            fragment.setArguments(data);
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Function to get data from API
    public static JSONObject getData(){

        JSONObject wdata = null;


        try {
            // Location is hardcoded in this work
            URL url = new URL("https://api.smartcitizen.me/v0/devices/9333");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp = "";

            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            wdata = new JSONObject(json.toString());

        } catch (Exception e) {

            Log.d("Exception", e.getMessage());
            return null;
        }
        return wdata;
    }
    //Class asynchronous to get the data and work on it without disturbing the whole application
    public static class WeatherTask extends AsyncTask<Void, Void, String> {

        public double _COratio=0.0133333;
        public double _NOratio=0.1;
        public WeatherTask() {

        }

        @Override
        protected String doInBackground(final Void... params) {
            Measurement test =dbDAO.getLastMeasurement();
            JSONObject result = getData();

            return result.toString();
        }

        @Override
        protected void onPostExecute(final String result) {//result de doInBackground

            try {
                JSONObject jsonGlobal = new JSONObject(result);//all information in the page
                JSONObject jsonData = jsonGlobal.getJSONObject("data");//JSON object containing data
                JSONArray jsonSensor = jsonData.getJSONArray("sensors");//JSON array containing sensors data
                JSONObject jsonTemp=jsonSensor.getJSONObject(3);//JSON object containing temperature information
                JSONObject jsonCO=jsonSensor.getJSONObject(4);//JSON object containing CO information
                JSONObject jsonNO2=jsonSensor.getJSONObject(5);//JSON object containing NO2 information
                JSONObject jsonHum=jsonSensor.getJSONObject(2);//JSON object containing humidity information
                JSONObject jsonNoise=jsonSensor.getJSONObject(2);//JSON object containing humidity information
                JSONObject jsonLight=jsonSensor.getJSONObject(7);//JSON object containing humidity information
                Measurement temp =new Measurement(
                        jsonTemp.getDouble("value"),
                        (jsonCO.getDouble("value"))*_COratio,
                        (jsonNO2.getDouble("value"))*_NOratio,
                        jsonHum.getDouble("value"),
                        jsonNoise.getDouble("value"),
                        jsonLight.getDouble("value"));

                dbDAO.addMeasurement(temp);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Log.d("JSON: ", result);
        }
    }
}
