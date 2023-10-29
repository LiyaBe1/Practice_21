package com.example.progbar

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var chrono: Chronometer
    private lateinit var startButton: Button
    private lateinit var stopButton: Button

    private var isRunning = false
    private var startTime: Long = 0
    private var pauseTime: Long = 0

    private val handler = Handler()
    private val updateTimeTask = object : Runnable {
        override fun run() {
            // выполнять этот код каждые 1000 ms (1 секунда == 1000 ms)
            val currentTime = SystemClock.elapsedRealtime() - chrono.base
            chrono.text = formatTime(currentTime)

            handler.postDelayed(this, 1000 / 60)
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chrono = findViewById(R.id.chronometer)
        startButton = findViewById(R.id.start_button)
        stopButton = findViewById(R.id.stop_button)

        startButton.setOnClickListener {
            if (!isRunning) {
                startTimer()
            }
        }

        stopButton.setOnClickListener {
            if (isRunning) {
                pauseTimer()
            }
        }
    }

    private fun startTimer() {
        chrono.base = SystemClock.elapsedRealtime() - pauseTime
        chrono.start()

        isRunning = true
        startTime = System.currentTimeMillis()
        handler.post(updateTimeTask)

        startButton.isEnabled = false
        stopButton.isEnabled = true
    }

    private fun pauseTimer() {
        chrono.stop()
        pauseTime = SystemClock.elapsedRealtime() - chrono.base

        isRunning = false
        handler.removeCallbacks(updateTimeTask)

        startButton.isEnabled = true
        stopButton.isEnabled = false
    }

    private fun formatTime(time: Long): String {
        val hours = (time / 3600000).toInt()
        val minutes = (time - hours * 3600000) / 60000
        val seconds = (time - hours * 3600000 - minutes * 60000) / 1000

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}