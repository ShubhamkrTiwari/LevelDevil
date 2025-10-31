package com.example.leveldevil20

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class Cloud(var x: Float, var y: Float, private val radius: Float) {

    private val paint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    fun draw(canvas: Canvas) {
        canvas.drawCircle(x, y, radius, paint)
        canvas.drawCircle(x + radius, y, radius, paint)
        canvas.drawCircle(x + radius / 2, y - radius / 2, radius, paint)
    }

    fun update(speed: Float) {
        x -= speed
    }
}