package com.shafi.saferide;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class TrackingService extends Service {

    private static final String CHANNEL_ID = "tracking_channel";
    private static final int NOTIFICATION_ID = 99;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        Log.d("TrackingService", "Service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Saferide is active")
                .setContentText("Location tracking is running")
                .setSmallIcon(R.drawable.ic_launcher_foreground)   // make sure this exists
                .setOngoing(true)
                .build();

        startForeground(NOTIFICATION_ID, notification);

        Log.d("TrackingService", "Service started");

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("TrackingService", "Service destroyed");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Tracking Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}
