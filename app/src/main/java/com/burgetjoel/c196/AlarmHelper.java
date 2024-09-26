package com.burgetjoel.c196;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.util.Calendar;

public class AlarmHelper {

    private final Context context;

    // Constructor to pass the context
    public AlarmHelper(Context context) {
        this.context = context;
    }

    // Method to check if the app can schedule exact alarms
    public boolean canScheduleExactAlarms() {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return alarmManager.canScheduleExactAlarms();
        }
        return true;
    }

    // Method to schedule the alarm
    public void scheduleExactAlarm(int hour, int minute, int day, int year, int month, String title, String message, Class<?> receiverClass) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager == null) {
            Log.e("AlarmHelper", "AlarmManager is null. Cannot schedule alarm.");
            return;
        }

        // Check if exact alarms can be scheduled
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !canScheduleExactAlarms()) {
            Log.d("AlarmHelper", "App cannot schedule exact alarms. Requesting permission...");
            // Request permission to schedule exact alarms
            Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
            context.startActivity(intent);
            return;
        }

        // Intent for the BroadcastReceiver
        Intent intent = new Intent(context, receiverClass);
        intent.putExtra("title", title);
        intent.putExtra("message", message);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Set the time for the alarm
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Schedule the alarm
        // For API 23 (Marshmallow) and above, allow while idle
        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                pendingIntent
        );

        Log.d("AlarmHelper", "Exact alarm scheduled for: " + calendar.getTime());
    }
}
