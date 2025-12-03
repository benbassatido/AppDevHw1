package com.example.hw1.utilities

import android.os.Handler
import android.os.Looper
import java.util.TimerTask

class Timer(
    private val intervalMillis: Long,
    private val onTick: () -> Unit
) {

    private var timer: java.util.Timer? = null
    private var timerOn: Boolean = false
    private val uiHandler = Handler(Looper.getMainLooper())

    fun start() {
        if (timerOn) return

        timerOn = true
        timer = java.util.Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                if (!timerOn) return

                uiHandler.post {
                    onTick()
                }
            }
        }, 0, intervalMillis)
    }

    fun stop() {
        timerOn = false
        timer?.cancel()
        timer = null
    }
}
