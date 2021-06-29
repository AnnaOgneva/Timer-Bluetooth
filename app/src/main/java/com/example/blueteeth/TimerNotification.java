package com.example.blueteeth;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class TimerNotification {

    public static final String CHANNEL_ID = "1";
//    public static final String CHANNEL_NAME = "Таймер";
//    public static final String TITLE_IN_PROCESS = "До выключения bluetooth осталось:";
//    public static final String TITLE_FINISHING = "Выключение bluetooth...";
    //Notification notification;

    public static void startTimerNotification(Context context, long timeLeft) {

        Intent intent = new Intent(context, TimerService.class);
        intent.setAction(TimerService.ACTION_START);
        intent.putExtra("timeLeft", timeLeft);
        //intent.putExtra("startTime", SharedPreferencesManager.getStartTime(context));
        //intent.putExtra("timeLeft", SharedPreferencesManager.getMinLatency(context));
        context.startService(intent);
    }
    public static void stopTimerNotification(Context context) {
        Intent intent = new Intent(context, TimerService.class);
        intent.setAction(TimerService.ACTION_STOP);
        context.startService(intent);
    }

    public static void pauseTimerNotification(Context context) {
        Intent intent = new Intent(context, TimerService.class);
        intent.setAction(TimerService.ACTION_PAUSE);
        context.startService(intent);
    }

    //@RequiresApi(api = Build.VERSION_CODES.O)
    public static Notification getNotification(Context context, long startTime, long timeLeft) {
        NotificationManagerCompat notificationManagerCompat =  NotificationManagerCompat.from(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, context.getResources().getString(R.string.notif_channel_name), NotificationManager.IMPORTANCE_LOW);
            notificationManagerCompat.createNotificationChannel(channel);
        }

        return createNotification(context,startTime, timeLeft,  context.getResources().getString(R.string.notif_title_inprocess));
    }

    public static Notification createNotification(Context context, long startTime, long timeLeft, String title) {
        long realTimeLeft = timeLeft;
        if(startTime != 0) {
            realTimeLeft = timeLeft - ((System.currentTimeMillis() / 10000) - startTime);
        }
        String contentText = TimeToText.convert(realTimeLeft);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
        if(timeLeft != 0) {
            notificationBuilder.setContentText(contentText);
        }
        Notification notification = notificationBuilder.setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setContentTitle(title).setSmallIcon(R.drawable.timer_icon).build();
        return notification;
    }


    public static void updateTimeLeft(Context context, long timeLeft) {
        Notification notification  = createNotification(context, 0, timeLeft, context.getResources().getString(R.string.notif_title_inprocess));
        NotificationManagerCompat notificationManagerCompat =  NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(TimerService.FOREGROUND_ID, notification);
    }
    public static void updateContentTitle(Context context) {
        Notification notification = createNotification(context, 0, 0, context.getResources().getString(R.string.notif_title_finishing));
        NotificationManagerCompat notificationManagerCompat =  NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(TimerService.FOREGROUND_ID, notification);
    }
    public static void setPauseState(Context context,  long timeLeft) {
        Notification notification = createNotification(context, 0, timeLeft, "Timer is on pause");
        NotificationManagerCompat notificationManagerCompat =  NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(TimerService.FOREGROUND_ID, notification);
    }
}
