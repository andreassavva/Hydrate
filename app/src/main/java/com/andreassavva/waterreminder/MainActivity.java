package com.andreassavva.waterreminder;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button showNotificationBtn, stopNotificationBtn, alarmBtn;
    NotificationManager notificationManager;
    boolean isNotificationActive = false;
    int notifId = 33;

    public static final String TAG = "AKS";
    private static final String PREFS_NAME = "WaterPrefsFile";

    private static boolean activityActive = false;

    SharedPreferences.Editor editor;

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    private Toolbar toolbar;

    private TextView progressAmountText, nextReminderText;

    private NavigationView mDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    SharedPreferences settings = null;

    private ProgressBar waterIntakeProgressBar, timeProgressBar;
    private Handler waterIntakeHandler = new Handler();
    private Handler timeHandler = new Handler();

    private int timeProgress;

    private String name, wdStart, wdEnd, weStart, weEnd, volumeMeasure;
    private int weight, levelOfActivity, dailyWater, cupSize;
    private boolean measureInKg, measureInMl;

    private int dailyProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /////////////////////// SHARED PREFERENCES ///////////////////////////////
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
        name = settings.getString("name", "");
        weight = settings.getInt("weight", 0);
        measureInKg = settings.getBoolean("measureInKg", true);
        levelOfActivity = settings.getInt("levelOfActivity", 2);

        wdStart = settings.getString("wdStart", "08:00");
        wdEnd = settings.getString("wdEnd", "22:00");
        weStart = settings.getString("weStart", "08:00");
        weEnd = settings.getString("weEnd", "22:00");

        dailyWater = settings.getInt("dailyWaterInMl", 0);
        measureInMl = settings.getBoolean("measureInMl", true);

        cupSize = settings.getInt("cupSize", 0);

        dailyProgress = settings.getInt("dailyProgress", 0);


        //////////////////////// TOOLBAR //////////////////////////////////////////
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ////////////////////////////// PROGRESS BARS /////////////////////////////////
        waterIntakeProgressBar = (ProgressBar) findViewById(R.id.waterIntakeProgressBar);
        timeProgressBar = (ProgressBar) findViewById(R.id.timeProgressBar);


        //////////////////////// COMPONENTS //////////////////////////////////////
        if (measureInMl) {
            volumeMeasure = " ml";
        } else {
            volumeMeasure = " oz";
        }
        progressAmountText = (TextView) findViewById(R.id.progressAmountTextView);
        nextReminderText = (TextView) findViewById(R.id.nextReminderTextView);
        waterIntakeProgressBar.setMax(dailyWater);
        updateProgress();


        //////////////////////// NAVIGATION DRAWER ///////////////////////////////
        mDrawer = (NavigationView) findViewById(R.id.main_drawer);
        mDrawer.setNavigationItemSelectedListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        ////////////////////// FLOATING ACTION BUTTON ////////////////////////////
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add button
                dailyProgress += cupSize;
                updateProgress();

                // Save dailyProgressInMl
                editor.putInt("dailyProgress", dailyProgress);
                editor.commit();

                updateTimeProgressBarColor();
            }
        });


    }

//    public void showNotification(View view) {
//        NotificationCompat.Builder notificationBuilder = new
//                NotificationCompat.Builder(this)
//                .setContentTitle("Message")
//                .setContentText("This is the message")
//                .setTicker("ALERT ")
//                .setSmallIcon(R.drawable.ic_announcement);
//
//        Intent moreInfoIntent = new Intent(this, MainActivity.class);
//
//        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
//        taskStackBuilder.addParentStack(MainActivity.class);
//        taskStackBuilder.addNextIntent(moreInfoIntent);
//
//        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//        notificationBuilder.setContentIntent(pendingIntent);
//
//        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(notifId, notificationBuilder.build());
//
//        isNotificationActive = true;
//    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_menu_daily_progress:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                if (!activityActive) {
                    intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.nav_menu_settings:

                break;
            case R.id.nav_menu_rate_us:

                break;
            case R.id.nav_menu_remove_ads:

                break;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityActive = true;
        updateTimeProgress();
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityActive = false;
    }

    public void updateProgress() {
        progressAmountText.setText(String.valueOf(dailyProgress) + "/" + String.valueOf(dailyWater) + volumeMeasure);
        waterIntakeProgressBar.setProgress(dailyProgress);
    }

    public void updateTimeProgress() {
        Calendar calendar = Calendar.getInstance();
        int currentMinutes = calendar.get(Calendar.MINUTE) + calendar.get(Calendar.HOUR_OF_DAY) * 60;
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);

        String start, end;
        if (currentDay == Calendar.SATURDAY || currentDay == Calendar.SUNDAY) {
            start = weStart;
            end = weEnd;
        } else {
            start = wdStart;
            end = wdEnd;
        }

        String tomorrowStart;
        if (currentDay == Calendar.SUNDAY) {
            tomorrowStart = wdStart;
        } else if (currentDay == Calendar.FRIDAY) {
            tomorrowStart = weStart;
        } else {
            tomorrowStart = start;
        }

        String[] startBrokenString = start.split(":");
        String[] endBrokenString = end.split(":");

        int startMinutes = Integer.parseInt(startBrokenString[1]) + Integer.parseInt(startBrokenString[0]) * 60;
        int endMinutes = Integer.parseInt(endBrokenString[1]) + Integer.parseInt(endBrokenString[0]) * 60;

        boolean endTimeOnNextDay = false;

        if (endMinutes - startMinutes < 0) {
            endTimeOnNextDay = true;
            endMinutes = Integer.parseInt(endBrokenString[1]) + Integer.parseInt(endBrokenString[0] + 24) * 60;
        }

        timeProgressBar.setMax(endMinutes - startMinutes);
        timeProgressBar.setProgress(currentMinutes - startMinutes);

        updateDate(calendar, endTimeOnNextDay, endMinutes, currentMinutes);
        updateTimeProgressBarColor();
        updateReminder(currentMinutes, startMinutes, endMinutes, tomorrowStart);
    }

    public void updateTimeProgressBarColor() {
        double timeRelProgress = (double) (timeProgressBar.getProgress()) / (timeProgressBar.getMax());
        double waterRelProgress = (double) (waterIntakeProgressBar.getProgress() + cupSize) / waterIntakeProgressBar.getMax();

        if (timeRelProgress > waterRelProgress) {
            timeProgressBar.setProgressDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.custom_progress_bar_red));
        } else {
            timeProgressBar.setProgressDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.custom_progress_bar_green));
        }
    }

    public void updateDate(Calendar calendar, Boolean endTimeOnNextDay, int endMinutes, int currentMinutes) {
        settings = getSharedPreferences(PREFS_NAME, 0);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String currentDate = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(dayOfMonth);

        if ((!settings.getString("date", "").equals(currentDate)) ||
                (settings.getString("date", "").equals(currentDate) && endTimeOnNextDay && currentMinutes > endMinutes)) {
            dailyProgress = 0;
            editor.putInt("dailyProgress", dailyProgress);
            editor.putString("date", currentDate);
            editor.commit();
        }
    }

    public void updateReminder(int currentMinutes, int startMinutes, int endMinutes, String tomorrowStart) {
        int reminderInterval = (endMinutes - startMinutes) / (dailyWater / cupSize);
        int minutesForNextReminder = 0;
        Calendar nextAlarm = Calendar.getInstance();

        int i = 1;
        while (currentMinutes > startMinutes + reminderInterval * i) {
            i++;
        }
        if (i >= dailyWater / cupSize) {
            nextReminderText.setText(tomorrowStart);
            String[] tomorrowStartStrings = tomorrowStart.split(":");
            nextAlarm.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tomorrowStartStrings[0].trim()));
            nextAlarm.set(Calendar.MINUTE, Integer.parseInt(tomorrowStartStrings[1].trim()));
        } else {
            minutesForNextReminder = startMinutes + reminderInterval * i;
            nextReminderText.setText(String.format("%02d", minutesForNextReminder / 60) + ":" + String.format("%02d", minutesForNextReminder % 60));
            nextAlarm.set(Calendar.HOUR_OF_DAY, minutesForNextReminder / 60);
            nextAlarm.set(Calendar.MINUTE, minutesForNextReminder % 60);
        }

        if (nextAlarm.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
            nextAlarm.add(Calendar.DATE, 1);
        }

        Long alertTime = nextAlarm.getTimeInMillis();
        Intent alertIntent = new Intent(this, AlertReceiver.class);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime,
                PendingIntent.getBroadcast(this, 1, alertIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT));
    }
}
