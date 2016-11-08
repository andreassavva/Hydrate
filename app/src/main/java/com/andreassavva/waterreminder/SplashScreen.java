package com.andreassavva.waterreminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class SplashScreen extends AppCompatActivity {

    public static final String TAG = "AKS";
    private static final String PREFS_NAME = "WaterPrefsFile";

    SharedPreferences settings = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //thread for splash screen running
        Thread logoTimer = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    Log.e(TAG, "Exception" + e);
                } finally {
                    settings = getSharedPreferences(PREFS_NAME, 0);
                    if (settings.getBoolean("firstrun", true)) {
                        // Do first run stuff here then set 'firstrun' as false
                        // using the following line to edit/commit prefs
                        startActivity(new Intent(SplashScreen.this, WelcomeScreen.class));
                    } else {
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    }
                }
            }
        };
        logoTimer.start();


    }

}