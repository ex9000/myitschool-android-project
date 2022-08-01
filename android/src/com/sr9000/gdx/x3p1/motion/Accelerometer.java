package com.sr9000.gdx.x3p1.motion;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Accelerometer implements SensorEventListener {

    public float x, y, z;

    SensorManager sensorManager;
    Sensor accelerometer;

    public Accelerometer(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
//        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    public void onPause() {
        sensorManager.unregisterListener(this);
    }

    public void onResume() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        x = -event.values[0];
        y = -event.values[1];
        z = -event.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // nop
    }

}
