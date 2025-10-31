package com.example.leveldevil20

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect

class Spike(var x: Int, var y: Int, var width: Int, var height: Int) {
    private val paint = Paint()
    val rect: Rect
        get() = Rect(x, y, x + width, y + height)

    init {
        paint.color = Color.rgb(160, 82, 45) // Sienna
        paint.style = Paint.Style.FILL
    }

    fun draw(canvas: Canvas) {
        val path = Path()
        val triangleWidth = height * 2
        val numTriangles = width / triangleWidth

        for (i in 0 until numTriangles) {
            val startX = x + i * triangleWidth
            path.moveTo(startX.toFloat(), y.toFloat() + height)
            path.lineTo(startX.toFloat() + triangleWidth / 2, y.toFloat())
            path.lineTo(startX.toFloat() + triangleWidth, y.toFloat() + height)
            path.close()
        }
        canvas.drawPath(path, paint)
    }
}