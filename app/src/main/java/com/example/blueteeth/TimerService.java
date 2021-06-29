package com.example.blueteeth;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class TimerService extends Service {
    public static final String ACTION_START = "om.example.blueteeth.ACTION_START";
   public static final String ACTION_STOP = "om.example.blueteeth.ACTION_STOP";
    public static final String ACTION_PAUSE = "om.example.blueteeth.ACTION_PAUSE";
    public static final int FOREGROUND_ID = 10;
    CountDownTimer countDownTimer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null) {
            switch (intent.getAction()) {
                case (ACTION_START):
                    long startTime = SharedPreferencesManager.getStartTime(this);
                    long timeLeft = intent.getLongExtra("timeLeft", 0);
                    startTimer(startTime, timeLeft);
                    break;
                case (ACTION_STOP):
                    stopTimer();
                    break;
                case (ACTION_PAUSE):
                    pauseTimer();
                    break;

            }
        }
        return START_NOT_STICKY;
    }

    private void startTimer(long startTime, long timeLeft) {
           this.startForeground(FOREGROUND_ID, TimerNotification.getNotification(this, startTime, timeLeft));
           countDownTimer = new CountDownTimer(timeLeft * 1000, 1000) {
               @Override
               public void onTick(long millisUntilFinished) {
                   if (millisUntilFinished / 1000 >= 3) {
                       TimerNotification.updateTimeLeft(getBaseContext(), millisUntilFinished / 1000);
                   } else {
                       TimerNotification.updateContentTitle(getBaseContext());
                   }
                   SharedPreferencesManager.editTimeLeft(getBaseContext(), millisUntilFinished / 1000);
               }

               @Override
               public void onFinish() {
                   stopTimer();
                   Log.d("myLogs", "onFinish timerservice");
                   SharedPreferencesManager.editTimeLeft(getBaseContext(), -1);
               }
           }.start();
    }

    private void stopTimer() {
        if(countDownTimer != null) {countDownTimer.cancel();}
        SharedPreferencesManager.editTimeLeft(getBaseContext(), -1);
        this.stopForeground(true);

    }

    private void pauseTimer() {
        if(countDownTimer != null) {countDownTimer.cancel();}
        TimerNotification.setPauseState(getBaseContext(), SharedPreferencesManager.getTimeLeft(getBaseContext()));

    }
}
