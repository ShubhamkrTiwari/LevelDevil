package com.example.leveldevil20

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class Enemy(var x: Int, var y: Int, var size: Int) {
    private val paint = Paint()
    val rect: Rect
        get() = Rect(x, y, x + size, y + size)

    init {
        paint.color = Color.RED
    }

    fun draw(canvas: Canvas) {
        canvas.drawRect(rect, paint)
    }
}