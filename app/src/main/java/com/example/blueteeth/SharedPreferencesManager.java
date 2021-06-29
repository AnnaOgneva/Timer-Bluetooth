package com.example.blueteeth;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String MINIMUM_LATENCY_KEY = "minimum_latency";
    private static final String OVERRIDE_DEADLINE_KEY = "override_deadline";
    private static final String START_TIME = "start_time";
    private static final String TIME_LEFT = "time_left";
    private static final String IS_ON_PAUSE ="is_on_pause";


//    public static boolean getTimerState(Context ctx) {
//        SharedPreferences sharedPreferences = ctx.getSharedPreferences("DisableBluetoothTimer", Context.MODE_PRIVATE);
//        return sharedPreferences.getBoolean(TIMER_IS_ON, false);
//    }
    public static long getMinLatency(Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("DisableBluetoothTimer", Context.MODE_PRIVATE);
        return sharedPreferences.getLong(MINIMUM_LATENCY_KEY, 0);
    }
    public static boolean getIsOnPause(Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("DisableBluetoothTimer", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_ON_PAUSE, false);
    }
//    public static long getOverrideDeadline(Context ctx) {
//        SharedPreferences sharedPreferences = ctx.getSharedPreferences("DisableBluetoothTimer", Context.MODE_PRIVATE);
//        return sharedPreferences.getLong(OVERRIDE_DEADLINE_KEY, 0);
//    }
    public static long getStartTime(Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("DisableBluetoothTimer", Context.MODE_PRIVATE);
        return sharedPreferences.getLong(START_TIME, 0);
    }
    public static long getTimeLeft(Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("DisableBluetoothTimer", Context.MODE_PRIVATE);
        return sharedPreferences.getLong(TIME_LEFT, -1);
    }
//    public static void editTimerState(Context ctx, boolean flag) {
//        SharedPreferences sharedPreferences = ctx.getSharedPreferences("DisableBluetoothTimer", Context.MODE_PRIVATE);
//        sharedPreferences.edit().putBoolean(TIMER_IS_ON, flag).apply();
//    }
    public static void editMinLatency(Context ctx, long minLatency) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("DisableBluetoothTimer", Context.MODE_PRIVATE);
        sharedPreferences.edit().putLong(MINIMUM_LATENCY_KEY, minLatency).apply();
    }
    public static void editOverrideDeadline(Context ctx, long overrideDeadline) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("DisableBluetoothTimer", Context.MODE_PRIVATE);
        sharedPreferences.edit().putLong(OVERRIDE_DEADLINE_KEY, overrideDeadline).apply();
    }
    public static void editStartTime(Context ctx, long starttime) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("DisableBluetoothTimer", Context.MODE_PRIVATE);
        sharedPreferences.edit().putLong(START_TIME, starttime).apply();
    }
    public static void editTimeLeft(Context ctx, long timeLeft) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("DisableBluetoothTimer", Context.MODE_PRIVATE);
        sharedPreferences.edit().putLong(TIME_LEFT, timeLeft).apply();
    }
    public static void editIsOnPause(Context ctx, boolean isOnPause) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("DisableBluetoothTimer", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(IS_ON_PAUSE, isOnPause).apply();
    }

}
