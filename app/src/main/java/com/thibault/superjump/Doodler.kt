package com.thibault.superjump

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class Doodler(private var x: Float, private var y: Float) {
    private val radius = 50f

    fun draw(canvas: Canvas?, paint: Paint) {
        paint.color = Color.BLUE
        canvas?.drawCircle(x, y, radius, paint)
    }

    fun move(dx: Float) {
        x += dx * 10 // Déplacement horizontal basé sur l'inclinaison
    }
}
