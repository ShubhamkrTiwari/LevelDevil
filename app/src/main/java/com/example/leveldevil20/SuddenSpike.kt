package com.example.leveldevil20

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect

class SuddenSpike(var x: Int, var y: Int, var width: Int, var height: Int, private val onTime: Int, private val offTime: Int, private var timer: Int = 0) {

    private val paint = Paint().apply {
        color = Color.rgb(205, 92, 92) // Match background color
        style = Paint.Style.FILL
    }

    var isVisible = false

    val rect: Rect
        get() = Rect(x, y, x + width, y + height)

    fun update() {
        timer = (timer + 1) % (onTime + offTime)
        isVisible = timer < onTime
    }

    fun draw(canvas: Canvas) {
        if (isVisible) {
            val path = Path()
            path.moveTo(x.toFloat(), (y + height).toFloat())
            path.lineTo((x + width / 2).toFloat(), y.toFloat())
            path.lineTo((x + width).toFloat(), (y + height).toFloat())
            path.close()
            canvas.drawPath(path, paint)
        }
    }
}