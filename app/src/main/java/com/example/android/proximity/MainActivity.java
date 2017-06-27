package com.example.android.proximity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Chronometer;
import android.widget.TextView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    Sensor proximitySensor;
    Chronometer chronometer;
    MediaPlayer alarm;
    boolean play = false;
    Thread musicThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        chronometer = (Chronometer) findViewById(R.id.clock);
        alarm = MediaPlayer.create(getApplicationContext(),R.raw.alarm);
        alarm.setLooping(true);
        musicThread = new Thread(new Runnable() {
            @Override
            public void run() {
                alarm = MediaPlayer.create(getApplicationContext(), R.raw.alarm);
                SystemClock.sleep(5000);
                alarm.start();
                SystemClock.sleep(5000);
                play = false;
                alarm.stop();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,proximitySensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.values[0]==0){

            if(play==false) {

                musicThread.start();
            }
            play = true;
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
        }
        else {

            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.stop();
        }

    }
}
