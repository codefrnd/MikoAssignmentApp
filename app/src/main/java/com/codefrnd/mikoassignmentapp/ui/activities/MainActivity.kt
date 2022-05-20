package com.codefrnd.mikoassignmentapp.ui.activities

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.codefrnd.mikoassignmentapp.R
import com.codefrnd.mikoassignmentapp.data.preference.PreferenceProvider
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    var previousRuntime: Long = 0
    var startTime: Long = 0;
    val timerHandler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        previousRuntime = PreferenceProvider(this).getRunTime()
        startTime = System.currentTimeMillis();
    }

    private val mTickRunnable: Runnable = object : Runnable {
        override fun run() {
            val millis = System.currentTimeMillis() - startTime
            val seconds = previousRuntime + millis / 1000

            Log.d(TAG, "run: $seconds")

            PreferenceProvider(this@MainActivity).setRunTime(seconds)
            timerHandler.postDelayed(this, 1000)
        }
    }

    override fun onStart() {
        super.onStart()
        timerHandler.postDelayed(mTickRunnable, 0)
    }

    override fun onStop() {
        super.onStop()
        timerHandler.removeCallbacks(mTickRunnable)
    }
}