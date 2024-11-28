package com.doodlejump

import android.util.Log

class TimeObservable(var duration: Int, private val obj: GameObject) {

    var started = false
    val maxDuration = duration

    fun start() {
        started = true
    }

    fun update() {
        if(started) Log.d("", "$duration")
        if(started) duration --
        if(duration <= 0) obj.removed = true
    }
}