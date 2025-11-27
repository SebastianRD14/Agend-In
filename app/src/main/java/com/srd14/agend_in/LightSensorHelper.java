package com.srd14.agend_in;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class LightSensorHelper {

    // Interfaz para el callback
    public interface LightCallBack {
        void onLightChanged(float lux);

        void onModeChanged(boolean isDark);
    }

    // Atributos
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private LightCallBack callback;
    private SensorEventListener sensorEventListener;

    // Variables de control del modo oscuro
    private boolean isDarkMode = false;
    private static final float DARK_THRESHOLD = 30f;
    private static final float LIGHT_THRESHOLD = 90f;


    // Constructor
    public LightSensorHelper(Context context, LightCallBack callback) {
        this.callback = callback;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);



        // Event listener del sensor
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float luz = event.values[0];

                if (!isDarkMode && luz <= DARK_THRESHOLD) {
                    isDarkMode = true;
                    callback.onModeChanged(true);  // modo oscuro
                } else if (isDarkMode && luz >= LIGHT_THRESHOLD) {
                    isDarkMode = false;
                    callback.onModeChanged(false); // modo claro
                }
            }

            // No se usa para este sensor de luz pero se necesita agregar
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };

    }

    // Ver si existe el sensor
    public boolean hasLightSensor() {
        return lightSensor != null;
    }

    // Iniciar y detener el sensor
    public void start() {
        if (lightSensor != null)
            sensorManager.registerListener(sensorEventListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        sensorManager.unregisterListener(sensorEventListener);
    }

}
