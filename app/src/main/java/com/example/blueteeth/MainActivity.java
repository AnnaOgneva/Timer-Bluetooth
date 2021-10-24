package com.example.blueteeth;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;


public class MainActivity extends AppCompatActivity {

    long[] spinner_values;

    ImageView timer_ring;
    ImageButton pause_btn, start_btn, stop_btn;
    Spinner set_time;
    TextView textView_timer, textView_add_time;
    CountDownTimer countDownTimerOnButton;

    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner_values = new long[]{15, 600, 1800, 3600, 5400, 7200};
        SharedPreferencesManager.editMinLatency(this, spinner_values[0]);
        SharedPreferencesManager.editOverrideDeadline(this, spinner_values[0] + 2);

        createManageButtons();
        createSpinner();
        createTextTimer();

        timer_ring = findViewById(R.id.timer_ring);

        textView_add_time = (TextView) findViewById(R.id.text_add_time);
        textView_add_time.setClickable(true);
        textView_add_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DisableBluetoothScheduler.isJobInQueue(getBaseContext())) {
                    TimerNotification.stopTimerNotification(getBaseContext());
                    DisableBluetoothScheduler.pauseJob(getBaseContext());
                    countDownTimerOnButton.cancel();
                    long timeLeft = SharedPreferencesManager.getTimeLeft(getBaseContext()) + 60;
                    DisableBluetoothScheduler.scheduleJob(getBaseContext(), timeLeft);
                    TimerNotification.startTimerNotification(getBaseContext(), timeLeft);
                    startTextTimer(timeLeft);
                }
            }
        });


    }

    @Override
    protected void onResume() {
        handler.removeCallbacks(uiUpdaterRunnable);
        handler.postDelayed(uiUpdaterRunnable, 100);
        super.onResume();
    }

    @Override
    protected void onStop() {
        handler.removeCallbacks(uiUpdaterRunnable);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void createManageButtons() {
        pause_btn = (ImageButton) findViewById(R.id.pause_btn);
        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DisableBluetoothScheduler.isJobInQueue(getBaseContext())) {
                    DisableBluetoothScheduler.pauseJob(getBaseContext());
                    countDownTimerOnButton.cancel();
                    TimerNotification.pauseTimerNotification(getBaseContext());
                }
            }
        });

        start_btn = (ImageButton) findViewById(R.id.start_btn);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!DisableBluetoothScheduler.isJobInQueue(getBaseContext())) {
                    long timeLeft = SharedPreferencesManager.getTimeLeft(getBaseContext());
                    Log.d("myLogs", "timeLeft = " + timeLeft);
                    if(timeLeft == -1) {timeLeft = SharedPreferencesManager.getMinLatency(getBaseContext());}
                    DisableBluetoothScheduler.scheduleJob(getBaseContext(), timeLeft);
                    TimerNotification.startTimerNotification(getBaseContext(), timeLeft);
                    startTextTimer(timeLeft);
                }
            }
        });

        stop_btn = (ImageButton) findViewById(R.id.stop_btn);
        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DisableBluetoothScheduler.isJobInQueue(getBaseContext()) || SharedPreferencesManager.getTimeLeft(getBaseContext()) != -1) {
                    DisableBluetoothScheduler.stopJob(getBaseContext());
                    TimerNotification.stopTimerNotification(getBaseContext());
                    if(countDownTimerOnButton != null) {countDownTimerOnButton.cancel();}
                    updateTextTimer(0, false);
                }
            }
        });
    }

    public void createSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,  getResources().getStringArray(R.array.spinner_items));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        set_time = findViewById(R.id.set_time);
        set_time.setAdapter(adapter);
        set_time.setSelection(0);
        set_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferencesManager.editMinLatency(getBaseContext(), spinner_values[position]);
                SharedPreferencesManager.editOverrideDeadline(getBaseContext(), spinner_values[position] + 10);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void createTextTimer() {
        textView_timer = findViewById(R.id.text_timer);
        long timeLeft = SharedPreferencesManager.getTimeLeft(this);
        boolean isJobInQueue = DisableBluetoothScheduler.isJobInQueue(this);
        if(isJobInQueue && timeLeft != -1) {
            updateTextTimer(timeLeft, true);
            startTextTimer(timeLeft);
        } else if (!isJobInQueue && timeLeft != -1) {
            updateTextTimer(timeLeft, true);
        }
    }

    public void updateTimerButton() {
        boolean isJobInQueue = DisableBluetoothScheduler.isJobInQueue(this);
    }

    public void updateTextTimer(long timeLeft, boolean isRunning) {
        String text = "";
        if(isRunning) {
            text = TimeToText.convert(timeLeft);
        } else {
            text = "00:00:00";
        }
        textView_timer.setText(text);
    }


    public void startTextTimer(long timeLeft) {
        countDownTimerOnButton = new CountDownTimer(timeLeft * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTextTimer(millisUntilFinished / 1000, true);
            }

            @Override
            public void onFinish() {
                updateTextTimer(0, false);
            }
        }.start();
    }

    private Runnable uiUpdaterRunnable = new Runnable() {
        @Override
        public void run() {
            updateUI();
            handler.postDelayed(this, 100);
        }
    };

    public void updateUI() {
        boolean isJobIQueue = DisableBluetoothScheduler.isJobInQueue(this);
        if(isJobIQueue) {
            //timer_ring.setColorFilter(getResources().getColor(R.color.Blue_300));
            //timer_ring.setImageDrawable(getResources().getDrawable(R.drawable.ic_circle));
            timer_ring.setImageResource(R.drawable.ic_circle_l);
            textView_timer.setTextColor(getResources().getColor(R.color.Blue_300));
            textView_add_time.setTextColor(getResources().getColor(R.color.Blue_300));
            start_btn.setColorFilter(getResources().getColor(R.color.Blue_300));
            //stop_btn.setColorFilter(getResources().getColor(R.color.Gray_500));
            pause_btn.setColorFilter(getResources().getColor(R.color.Gray_500));

        } else if (!isJobIQueue && SharedPreferencesManager.getTimeLeft(this) != -1){     //pause
            timer_ring.setImageResource(R.drawable.ic_circle_l);
            textView_timer.setTextColor(getResources().getColor(R.color.Gray_500));
            textView_add_time.setTextColor(getResources().getColor(R.color.Gray_500));
            start_btn.setColorFilter(getResources().getColor(R.color.Gray_500));
            pause_btn.setColorFilter(getResources().getColor(R.color.Blue_300));
            //stop_btn.setColorFilter(getResources().getColor(R.color.Gray_500));
        } else {
            timer_ring.setImageResource(R.drawable.ic_circle);
            Log.d("myLogs", "stop done");
            textView_timer.setTextColor(getResources().getColor(R.color.Gray_500));
            textView_add_time.setTextColor(getResources().getColor(R.color.Gray_500));
            start_btn.setColorFilter(getResources().getColor(R.color.Gray_500));
            //stop_btn.setColorFilter(getResources().getColor(R.color.Gray_500));
            pause_btn.setColorFilter(getResources().getColor(R.color.Gray_500));
        }
    }



}