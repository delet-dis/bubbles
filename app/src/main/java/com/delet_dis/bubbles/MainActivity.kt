package com.delet_dis.bubbles

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.delet_dis.bubbles.databinding.ActivityMainBinding
import com.delet_dis.bubbles.view.MoBikeTagLayout

class MainActivity : AppCompatActivity() {

  private lateinit var moBikeTagLayout: MoBikeTagLayout
  private lateinit var sensorManager: SensorManager
  private lateinit var accelerometerSensor: Sensor
  private lateinit var sensorEventListener: SensorEventListener

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater)
    val view = binding.root

    setContentView(view)

    sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
    accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    moBikeTagLayout = binding.moBikeLayout

    sensorEventListener = object : SensorEventListener {
      override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
          val x = event.values[0]
          val y = event.values[1]
          moBikeTagLayout.onSensorChanged(x, y)
        }
      }

      override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    moBikeTagLayout.setOnClickListener {
      val randomSize = (50..300).random()
      val imageView = ImageView(applicationContext)
      imageView.setImageResource(R.drawable.bubble_vector)
      imageView.setColorFilter(Color.argb(255, (0..255).random(), (0..255).random(), (0..255).random()))
      moBikeTagLayout.addView(imageView)
      imageView.layoutParams.height = randomSize
      imageView.layoutParams.width = randomSize
    }
  }

  override fun onResume() {
    super.onResume()
    sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_UI)
  }

  override fun onPause() {
    super.onPause()
    sensorManager.unregisterListener(sensorEventListener)
  }

}
