package com.doodlejump

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var game: GameManager
    private lateinit var sensor: Sensor
    private lateinit var sensorManager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        game = findViewById(R.id.mainView)
        game.activity = this
        game.setZOrderOnTop(true)
        game.holder.setFormat(PixelFormat.TRANSPARENT)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager;
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)!!;
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        game.onResume()
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        game.onPause()
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        game.setXOrientation(-(p0!!.values[2] / Math.PI * 180).toFloat())
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    fun endScreen(score: Int) {
        Intent(baseContext, ReplayActivity::class.java).also {
            it.putExtra("SCORE", score)
            startActivity(it) }
        finish()
    }

}