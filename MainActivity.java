package com.gonzalez.rafa.reproductoraautomatico;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity implements SensorEventListener {

    /**
     * Constants for sensors
     */
    private static final float SHAKE_THRESHOLD = 1.1f;
    private static final int SHAKE_WAIT_TIME_MS = 250;
    private static final float ROTATION_THRESHOLD = 2.0f;
    private static final int ROTATION_WAIT_TIME_MS = 100;

    /**
     * The sounds to play when a pattern is detected
     */
    private static MediaPlayer sonidoAcel;
    private static MediaPlayer soundGyro;

    /**
     * Sensors
     */
    private SensorManager mSensorManager;
    private Sensor sAcelerometro;
    private Sensor sGiroscopio;
    private long mShakeTime = 0;
    private long mRotationTime = 0;

    /**
     * UI
     */
    private TextView mGyrox;
    private TextView mGyroy;
    private TextView mGyroz;
    private TextView mAccx;
    private TextView mAccy;
    private TextView mAccz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the sensors to use
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sAcelerometro = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //sGiroscopio = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // Instanciate the sound to use
        sonidoAcel = MediaPlayer.create(this, R.raw.auxilio);
        //soundGyro = MediaPlayer.create(this, R.raw.auxilio);

        // mGyrox = (TextView) findViewById(R.id.gyro_x);
        // mGyroy = (TextView) findViewById(R.id.gyro_y);
        // mGyroz = (TextView) findViewById(R.id.gyro_z);

        mAccx = (TextView) findViewById(R.id.accele_x);
        mAccy = (TextView) findViewById(R.id.accele_y);
        mAccz = (TextView) findViewById(R.id.accele_z);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, sAcelerometro, SensorManager.SENSOR_DELAY_NORMAL);
      // mSensorManager.registerListener(this, sGiroscopio, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                mAccx.setText(R.string.act_main_no_acuracy);
                mAccy.setText(R.string.act_main_no_acuracy);
                mAccz.setText(R.string.act_main_no_acuracy);
            } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                mGyrox.setText(R.string.act_main_no_acuracy);
                mGyroy.setText(R.string.act_main_no_acuracy);
                mGyroz.setText(R.string.act_main_no_acuracy);
            }
            return;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mAccx.setText("x = " + Float.toString(event.values[0]));
            mAccy.setText("y = " + Float.toString(event.values[1]));
            mAccz.setText("z = " + Float.toString(event.values[2]));
            eventoAcelerometro(event);
        }
        else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            mGyrox.setText("x = " + Float.toString(event.values[0]));
            mGyroy.setText("y = " + Float.toString(event.values[1]));
            mGyroz.setText("z = " + Float.toString(event.values[2]));
            eventoGiroscopio(event);
        }

    }

    private void eventoAcelerometro(SensorEvent event) {
        long now = System.currentTimeMillis();

        if ((now - mShakeTime) > SHAKE_WAIT_TIME_MS) {
            mShakeTime = now;

            float gZ = event.values[2];
            if(gZ<=0){
                sonidoAcel.pause();
            }
            else{
                if(sonidoAcel.isPlaying()){

                }else {
                    sonidoAcel.start();
                }
            }
        }

    }

    private void eventoGiroscopio(SensorEvent event) {
        long now = System.currentTimeMillis();

        if ((now - mRotationTime) > ROTATION_WAIT_TIME_MS) {
            mRotationTime = now;


            if (Math.abs(event.values[0]) > ROTATION_THRESHOLD ||
                    Math.abs(event.values[1]) > ROTATION_THRESHOLD ||
                    Math.abs(event.values[2]) > ROTATION_THRESHOLD) {
                soundGyro.start();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}