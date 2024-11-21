package com.thibault.superjump

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class PlatformManager {
    private val platforms = mutableListOf<Platform>()

    fun addPlatforms(count: Int, screenWidth: Int, screenHeight: Int) {
        for (i in 0 until count) {
            val x = (0..screenWidth - 200).random().toFloat()
            val y = screenHeight - i * 150f
            platforms.add(Platform(x, y, 200f, 20f))
        }
    }

    fun drawPlatforms(canvas: Canvas, paint: Paint) {
        paint.color = Color.GREEN
        for (platform in platforms) {
            platform.draw(canvas, paint)
        }
    }
}
