package com.cyberstep.sanriocharacters;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/* loaded from: classes.dex */
public class SensorListener implements SensorEventListener {
    Sensor grav;
    Sensor mag;
    SensorManager sensorman;
    float[] mag_val = new float[3];
    float[] grav_val = new float[3];

    public void wakeupSensorManager(Context context) {
        this.sensorman = (SensorManager) context.getSystemService("sensor");
    }

    private int startMagneticField() {
        if (this.mag == null) {
            this.mag = this.sensorman.getDefaultSensor(2);
        }
        this.sensorman.registerListener(this, this.mag, 2);
        return 0;
    }

    private int stopMagneticField() {
        if (this.mag != null) {
            this.sensorman.unregisterListener(this, this.mag);
            return 0;
        }
        return 0;
    }

    private int startGravitySensor() {
        if (this.grav == null) {
            this.grav = this.sensorman.getDefaultSensor(9);
        }
        this.sensorman.registerListener(this, this.grav, 2);
        return 0;
    }

    private int stopGravitySensor() {
        if (this.grav != null) {
            this.sensorman.unregisterListener(this, this.grav);
            return 0;
        }
        return 0;
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent e) {
        switch (e.sensor.getType()) {
            case 2:
                this.mag_val = (float[]) e.values.clone();
                break;
            case 9:
                this.grav_val = (float[]) e.values.clone();
                break;
        }
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public float[] calcCurrentRotationMA(float[] accel) {
        float[] rotmat = new float[16];
        float[] idenmat = new float[16];
        float[] adjust = new float[3];
        SensorManager.getRotationMatrix(rotmat, idenmat, accel, this.mag_val);
        SensorManager.getOrientation(rotmat, adjust);
        return adjust;
    }

    public float[] getMagneticValue() {
        return this.mag_val;
    }

    public float[] getGravityValue() {
        return this.grav_val;
    }
}
