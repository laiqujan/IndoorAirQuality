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
//import for notification
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.support.v4.app.NotificationCompat;
import android.os.Build;
import android.provider.Settings;
import java.util.Calendar;
import java.util.Date;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //Variable for API Data fetch
    public static WeatherTask weatherTask = new WeatherTask();
    private static MeasurementDAO dbDAO;
    private static Context mainAppContext;
    //limit value for data
    public static int levels []={0,0,0};//table of actual level of danger for {CO,NO2,Hum} 0 = good quality / 1 = going worst / 2 = very bad quality
    //use for notification, we send notification when we pass in a different danger level
    public static double limit_COMax = 15.00;
    public static double limit_COGood = 9.00;
    public static double limit_NOGood = 3.50;
    public static double limit_NOMax = 4.00;
    public static double limit_HumDry= 25.00;
    public static double limit_HumWet = 75.00;
    private static String channel_name="Air Quality"; //name of the channel as in seen in exercise work
    private static String CHANNEL_ID="notify_001";//channel name as in seen in exercise work
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

    // Function to check if the value fetch are good for human
    public static void checkValue(Measurement _measur){
    // check the value and actual danger level to avoid plenty of notification, we notify the user only once when it's going worst or better

        // CO notification
        if(_measur.get_CO()>limit_COGood){//bad quality of air
            if(_measur.get_CO()<limit_COMax && levels[0]==0){// danger area 1 and before the qulity of air was good
                String msg = "Ambient Air is going worst, you should open the window. Actual CO amount : "+ String.format(" %.2f", _measur.get_CO())+"ppm";
                levels[0]=1;// we pass in level 1
                NotifyAboutData(mainAppContext,msg);
            }
            else if(_measur.get_CO()<limit_COMax && levels[0]==2){// danger zone 1 and before the qulity of air was very bad
                String msg = "Ambient Air is going better but still not good. Actual CO amount : "+ String.format(" %.2f", _measur.get_CO())+"ppm";
                levels[0]=1;// we pass in level 1
                NotifyAboutData(mainAppContext,msg);
            }
            else if(_measur.get_CO()>limit_COMax && (levels[0]==1 || levels[0]==0) ){
                String msg = "Ambient Air is very bad, you should open the window. Actual CO amount : "+ String.format(" %.2f", _measur.get_CO())+"ppm";
                levels[0]=2;// we pass in level 2
                NotifyAboutData(mainAppContext,msg);
            }
        }
        else{//the air quality is in the good area = danger area 0
            if(levels[0]==1 || levels[0] == 2){//if before we were in the danger area 1 or 2 we decrease the level
                String msg = "Ambient Air is good. Actual CO amount : "+ String.format(" %.2f", _measur.get_CO())+"ppm";
                levels[0]=0;
                NotifyAboutData(mainAppContext,msg);
            }
        }

        //NO2 notification
        if(_measur.get_NO2()>limit_NOGood){//bad quality of air
            if(_measur.get_CO()<limit_COMax && levels[1]==0){// danger area 1 and before the qulity of air was good
                String msg = "Ambient Air is going worst, you should open the window. Actual NO2 amount : "+ String.format(" %.2f",_measur.get_NO2())+"ppm";
                levels[1]=1;// we pass in level 1
                NotifyAboutData(mainAppContext,msg);
            }
            else if(_measur.get_NO2()<limit_NOMax && levels[1]==2){// danger zone 1 and before the qulity of air was very bad
                String msg = "Ambient Air is going better but still not good. Actual NO amount : "+ String.format(" %.2f", _measur.get_NO2())+"ppm";
                levels[1]=1;// we pass in level 1
                NotifyAboutData(mainAppContext,msg);
            }
            else if(_measur.get_NO2()>limit_NOMax && (levels[1]==1 || levels[1]==0) ){
                String msg = "Ambient Air is very bad, you should open the window. Actual NO2 amount : "+ String.format(" %.2f", _measur.get_NO2())+"ppm";
                levels[1]=2;// we pass in level 2
                NotifyAboutData(mainAppContext,msg);
            }
        }
        else{//the air quality is in the good area = danger area 0
            if(levels[1]==1 || levels[1] == 2){//if before we were in the danger area 1 or 2 we decrease the level
                String msg = "Ambient Air is Good. Actual NO2 amount : "+ String.format(" %.2f", _measur.get_NO2())+"ppm";
                levels[1]=0;
                NotifyAboutData(mainAppContext,msg);
            }
        }

        //Humidity
        if(limit_HumDry < _measur.get_humidity() && _measur.get_humidity() < limit_HumWet && levels[2]!=0) {
            String msg = " Humidity in ambient Air is better. Actual humidity : "+ String.format(" %.2f", _measur.get_humidity() )+"%";
            levels[2]=0;// we pass in level 0
            NotifyAboutData(mainAppContext,msg);
        }
        else {
            if(_measur.get_humidity() < limit_HumDry && levels[2]!=1){
                String msg = "Ambient Air is too dry you should turn down the air condition. Actual humidity : "+ String.format(" %.2f", _measur.get_humidity() )+"%";
                levels[2]=1;// we pass in level 1
                NotifyAboutData(mainAppContext,msg);
            }
            if(_measur.get_humidity() > limit_HumWet && levels[2]!=2){
                String msg = "Ambient Air is too wet you should turn on the air condition. Actual humidity : "+ String.format(" %.2f", _measur.get_humidity() )+"%";
                levels[2]=2;// we pass in level 2
                NotifyAboutData(mainAppContext,msg);
            }
        }

    }
    public static void NotifyAboutData(Context c, String NotifText){


        NotificationManager notificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
// What happens, e.g., what activity is launched, if notification is clicked
        Intent intent = new Intent(c,HomeActivity.class);
// Since this can happen in the future, wrap it in a pending intent
        PendingIntent pIntent = PendingIntent.getActivity(c, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        //Builder to help creating a Notification
        NotificationCompat.Builder nBuilder =new NotificationCompat.Builder(c,"");
        //Class to define style of the notification
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(NotifText);
        bigText.setBigContentTitle("Air Quality");
        bigText.setSummaryText("Bad Quality of your ambient air");

        nBuilder.setContentIntent(pIntent);
        nBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        nBuilder.setContentTitle("Weather");
        nBuilder.setContentText(NotifText);
        nBuilder.setPriority(Notification.PRIORITY_MAX);
        nBuilder.setStyle(bigText);
        long vibration[]={0,200,100,200,100,200};
        nBuilder.setVibrate(vibration);
        nBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = channel_name;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);
        }
        //send notification
        notificationManager.notify(0, nBuilder.build());

    }
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
                        jsonLight.getDouble("value"),
                        jsonData.getString("recorded_at"));
                dbDAO.addMeasurement(temp);
                checkValue(temp);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Log.d("JSON: ", result);
        }
    }
}
