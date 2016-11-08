package com.andreassavva.waterreminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotification(context, "Hydrate", "Time to drink water", "Alert");
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
