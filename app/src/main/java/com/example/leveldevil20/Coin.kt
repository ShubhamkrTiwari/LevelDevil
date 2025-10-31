package com.example.leveldevil20

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class Coin(var x: Int, var y: Int, var size: Int) {
    private val paint = Paint()
    val rect: Rect
        get() = Rect(x, y, x + size, y + size)

    init {
        paint.color = Color.YELLOW
    }

    fun draw(canvas: Canvas) {
        canvas.drawCircle((x + size / 2).toFloat(), (y + size / 2).toFloat(), (size / 2).toFloat(), paint)
    }
}