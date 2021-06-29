package com.example.blueteeth;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class DisableBluetoothJobService extends JobService {


    @Override
    public boolean onStartJob(JobParameters params) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.disable();
            }
        }
        jobFinished(params, false);
        return false;
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }


}
