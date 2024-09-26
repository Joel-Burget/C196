package com.burgetjoel.c196;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {

    private static final String CHANNEL_ID = "example_channel_id";
    private static final String CHANNEL_NAME = "Example Channel";
    private static final String CHANNEL_DESCRIPTION = "This is an example notification channel.";

    // Create a notification channel (required for Android 8.0+)
    public static void createNotificationChannel(Context context) {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.setDescription(CHANNEL_DESCRIPTION);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    // Show a notification
    public static void showNotification(Context context, String title, String message) {
        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)  // Notification icon
                .setContentTitle(title)  // Notification title
                .setContentText(message)  // Notification message
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)  // Notification priority
                .setAutoCancel(true);  // Auto-dismiss when clicked

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }
}
