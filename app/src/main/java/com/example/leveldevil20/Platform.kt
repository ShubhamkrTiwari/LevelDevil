package com.example.leveldevil20

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class Platform(var x: Int, var y: Int, var width: Int, var height: Int) {
    private val paint = Paint()
    val rect: Rect
        get() = Rect(x, y, x + width, y + height)

    init {
        paint.color = Color.rgb(160, 82, 45) // Sienna - a reddish-brown color
    }

    fun draw(canvas: Canvas) {
        canvas.drawRect(rect, paint)
    }
}