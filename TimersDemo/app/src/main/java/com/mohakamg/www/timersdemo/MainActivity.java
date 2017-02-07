package com.mohakamg.www.timersdemo;

import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Handler handler = new Handler();
        Runnable ruxn = new Runnable() {
            @Override
            public void run() {
                i += 1;
                Log.i("Runnable ", "Runnable Running " + Integer.toString(i));
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(ruxn);

        new CountDownTimer(10000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                Log.i("Seconds Left", String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                Log.i("Done!", "Countdown Timer Finished");
            }
        }.start();


    }
}
