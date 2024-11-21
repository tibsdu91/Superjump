package com.thibault.superjump

import android.graphics.Canvas
import android.graphics.Paint

class Platform(private var x: Float, private var y: Float, private val width: Float, private val height: Float) {
    fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawRect(x, y, x + width, y + height, paint)
    }
}
