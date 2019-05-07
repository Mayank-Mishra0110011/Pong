package com.ghost.pong;

import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

public class GameActivity extends AppCompatActivity implements SensorEventListener {
    private CustomView canvas;
    private SensorManager sensorManager;
    Sensor accelerometer;
    Connection connection = new Connection();
    private DatabaseReference databaseReference;
    private static String TAG = "GameActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        canvas = findViewById(R.id.Canvas);
        databaseReference = FirebaseDatabase.getInstance().getReference("P1x");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(GameActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        databaseReference.child("P1x").setValue(x);
        if (x < 0) {
            canvas.moveRight(canvas.lev2);
        }
        else if (x > 0) {
            canvas.moveLeft(canvas.lev2);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connection, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(connection);
    }

    public AlertDialog.Builder buildDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Connection Lost");
        builder.setMessage("You have lost connection with the server. Check your internet connection and try again.");
        builder.setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                startActivity(getIntent());
            }
        });
        return builder;
    }
}
