package com.delet_dis.bubbles;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.delet_dis.bubbles.view.MoBikeTagLayout;

public class MainActivity extends AppCompatActivity {

  MoBikeTagLayout moBikeTagLayout;

  private SensorManager sensorManager;
  private Sensor accelerometerSensor;
  private SensorEventListener sensorEventListener;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	moBikeTagLayout = findViewById(R.id.moBikeLayout);

	sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

	if (sensorManager != null) {
	  accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}

	sensorEventListener = new SensorEventListener() {
	  @Override
	  public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

		  float x = event.values[0];
		  float y = event.values[1];

		  moBikeTagLayout.onSensorChanged(x, y);
		}
	  }

	  @Override
	  public void onAccuracyChanged(Sensor sensor, int accuracy) {

	  }
	};
  }

  @Override
  protected void onResume() {
	super.onResume();
	sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
  }

  @Override
  protected void onPause() {
	super.onPause();
	sensorManager.unregisterListener(sensorEventListener);
  }
}