package com.example.leveldevil20

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class Door(var x: Int, var y: Int, var width: Int, var height: Int) {
    private val paint = Paint().apply {
        color = Color.LTGRAY
    }
    private val borderPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    val rect: Rect
        get() = Rect(x, y, x + width, y + height)

    fun draw(canvas: Canvas) {
        canvas.drawRect(rect, paint)
        canvas.drawRect(rect, borderPaint)
    }
}