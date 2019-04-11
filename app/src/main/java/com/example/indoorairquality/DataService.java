package com.example.indoorairquality;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
public class DataService extends Service {
    public void onCreate(){
        super.onCreate();
        Log.d("tag", "Service onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // AlarmManager can also be used to repeat the action.

        final Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                new HomeActivity.WeatherTask().execute();
                Log.d("tag", "Service onStartCommand");
                Log.d("Handlers", "Called on main thread");
                handler.postDelayed(this, 60000);//each minute
            }
        };

        handler.post(runnableCode);

        return START_STICKY;
    }




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
