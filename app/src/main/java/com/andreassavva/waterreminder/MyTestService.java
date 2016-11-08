package com.andreassavva.waterreminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MyTestService extends IntentService {
    public MyTestService() {
        super("MyTestService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        createNotification(getApplicationContext(), "Hydrate", "Time to drink water", "Alert");
        Log.i("MyTestService", "Service running");
    }

    public void createNotification(Context context, String msg, String msgText, String msgAlert) {

        PendingIntent notificationIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_drop_24dp)
                .setContentTitle(msg)
                .setContentText(msgText)
                .setTicker(msgAlert);

        notificationBuilder.setContentIntent(notificationIntent);

        notificationBuilder.setDefaults(Notification.DEFAULT_ALL);

        notificationBuilder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notificationBuilder.build());

    }
}
