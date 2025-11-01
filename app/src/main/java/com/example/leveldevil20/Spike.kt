package com.example.leveldevil20

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect

class Spike(var x: Int, var y: Int, var width: Int, var height: Int) {
    private val paint = Paint().apply {
        color = Color.rgb(205, 92, 92) // Reddish-orange color to match the background
        style = Paint.Style.FILL
    }

    val rect: Rect
        get() = Rect(x, y, x + width, y + height)

    fun draw(canvas: Canvas) {
        val path = Path()
        path.moveTo(x.toFloat(), (y + height).toFloat())
        path.lineTo((x + width / 2).toFloat(), y.toFloat())
        path.lineTo((x + width).toFloat(), (y + height).toFloat())
        path.close()
        canvas.drawPath(path, paint)
    }
}