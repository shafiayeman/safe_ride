package com.shafi.saferide;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class SafeRideService extends Service {

    public static final String CHANNEL_ID = "saferide_channel";
    public static final int NOTIFICATION_ID = 1001;

    public static boolean trackingStarted = false;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        startForeground(NOTIFICATION_ID, buildPreTrackingNotification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            String action = intent.getAction();

            if ("ACTION_START_TRACKING".equals(action)) {
                trackingStarted = true;
                updateNotification(buildTrackingActiveNotification());
            }
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /* ---------------- NOTIFICATION STATES ---------------- */

    private Notification buildPreTrackingNotification() {

        Intent tapIntent = new Intent(this, SafeRideService.class);
        tapIntent.setAction("ACTION_START_TRACKING");

        PendingIntent tapPendingIntent = PendingIntent.getService(
                this,
                0,
                tapIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("SafeRide")
                .setContentText("Tap to start tracking")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(tapPendingIntent)
                .setOngoing(true)
                .build();
    }

    private Notification buildTrackingActiveNotification() {

        // Open SafeRide
        Intent openAppIntent = new Intent(this, TrackingActivity.class);
        PendingIntent openAppPendingIntent = PendingIntent.getActivity(
                this,
                0,
                openAppIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Emergency (dummy for now)
        Intent emergencyIntent = new Intent(this, SafeRideService.class);
        PendingIntent emergencyPendingIntent = PendingIntent.getService(
                this,
                1,
                emergencyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Hide notification (collapse)
        Intent hideIntent = new Intent(this, SafeRideService.class);
        PendingIntent hidePendingIntent = PendingIntent.getService(
                this,
                2,
                hideIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("SafeRide")
                .setSmallIcon(R.mipmap.ic_launcher)
                .addAction(0, "Tracking Active", null)
                .addAction(0, "Emergency", emergencyPendingIntent)
                .addAction(0, "Open SafeRide", openAppPendingIntent)
                .addAction(0, "Hide Notification", hidePendingIntent)
                .setOngoing(true)
                .build();
    }

    private void updateNotification(Notification notification) {
        NotificationManager manager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "SafeRide Safety",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
