package com.delet_dis.bubbles

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.delet_dis.bubbles.databinding.ActivityMainBinding
import com.delet_dis.bubbles.view.BubblesView

class MainActivity : AppCompatActivity() {

  private lateinit var bubblesLayout: BubblesView
  private lateinit var sensorManager: SensorManager
  private lateinit var accelerometerSensor: Sensor
  private lateinit var sensorEventListener: SensorEventListener

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater)
    val view = binding.root

    setContentView(view)

    initAccelerometer()

    bubblesLayout = binding.bubblesLayout

    sensorEventListener = object : SensorEventListener {
      override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
          val x = event.values[0]
          val y = event.values[1]
          bubblesLayout.onSensorChanged(x, y)
        }
      }

      override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    bubblesLayout.setOnTouchListener { v, event ->
      if (event?.action == MotionEvent.ACTION_DOWN) {
        view.performClick()

        generateBubble(event.x, event.y)
      }
      v?.onTouchEvent(event) ?: true
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

  private fun generateBubble(clickXNotCentered: Float, clickYNotCentered: Float) {
    val randomSize = (50..300).random()
    val clickXCentered = clickXNotCentered - randomSize / 2
    val clickYCentered = clickYNotCentered - randomSize / 2
    val bubbleImageView = ImageView(applicationContext)
    bubbleImageView.setImageResource(R.drawable.bubble_vector)
    bubbleImageView.setColorFilter(Color.argb(255, (0..255).random(), (0..255).random(), (0..255).random()))
    bubblesLayout.addView(bubbleImageView)
    bubbleImageView.x = clickXCentered
    bubbleImageView.y = clickYCentered
    bubbleImageView.layoutParams.height = randomSize
    bubbleImageView.layoutParams.width = randomSize
  }

  private fun initAccelerometer() {
    sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
    accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
  }

}
