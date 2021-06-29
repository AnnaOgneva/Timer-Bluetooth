package com.example.blueteeth;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;


import java.util.List;
import java.util.concurrent.TimeUnit;

public class DisableBluetoothScheduler {


    private static int JOB_ID = 1;

    public static void scheduleJob(Context context, long timeLeft) {
        ComponentName jobService = new ComponentName(context, DisableBluetoothJobService.class);
        JobInfo.Builder exerciseJobBuilder = new JobInfo.Builder(JOB_ID, jobService);


        exerciseJobBuilder.setMinimumLatency(TimeUnit.SECONDS.toMillis(timeLeft));
        exerciseJobBuilder.setOverrideDeadline(TimeUnit.SECONDS.toMillis(timeLeft));

//        exerciseJobBuilder.setMinimumLatency(TimeUnit.SECONDS.toMillis(15));
//        exerciseJobBuilder.setOverrideDeadline(TimeUnit.SECONDS.toMillis(15));

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(exerciseJobBuilder.build());
       // SharedPreferencesManager.editIsOnPause(context, false);
        SharedPreferencesManager.editStartTime(context, (System.currentTimeMillis() / 1000));
    }

    public static void pauseJob(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(JOB_ID);
       // SharedPreferencesManager.editIsOnPause(context, true);
    }

    public static void stopJob(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(JOB_ID);
       // SharedPreferencesManager.editIsOnPause(context, false);
        //SharedPreferencesManager.editTimeLeft(context, -1);
    }

    public static boolean isJobInQueue(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        List<JobInfo> list = jobScheduler.getAllPendingJobs();
        for(JobInfo ji : list) {
            if(ji.getId() == JOB_ID) {
                return true;
            }
        }
        return false;
    }


}
