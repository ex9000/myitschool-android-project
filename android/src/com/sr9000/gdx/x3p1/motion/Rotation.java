package com.sr9000.gdx.x3p1.motion;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Rotation implements SensorEventListener {

    public float wx, wy, wz;

    SensorManager sensorManager;
    Sensor gyroscope;

    public Rotation(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    public void onPause() {
        sensorManager.unregisterListener(this);
    }

    public void onResume() {
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        wx = event.values[0];
        wy = event.values[1];
        wz = event.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // nop
    }

}
