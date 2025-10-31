package com.example.leveldevil20

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect

class House(var x: Int, var y: Int, var width: Int, var height: Int) {

    private val houseBodyPaint = Paint().apply {
        color = Color.rgb(205, 133, 63) // Peru color
    }
    private val roofPaint = Paint().apply {
        color = Color.rgb(139, 69, 19) // SaddleBrown color
    }
    private val doorPaint = Paint().apply {
        color = Color.BLACK
    }

    val rect: Rect
        get() = Rect(x, y, x + width, y + height)

    fun draw(canvas: Canvas) {
        // Draw house body
        canvas.drawRect(rect, houseBodyPaint)

        // Draw roof
        val roofPath = Path()
        roofPath.moveTo(x.toFloat(), y.toFloat())
        roofPath.lineTo((x + width / 2).toFloat(), (y - height / 2).toFloat())
        roofPath.lineTo((x + width).toFloat(), y.toFloat())
        roofPath.close()
        canvas.drawPath(roofPath, roofPaint)

        // Draw door
        val doorWidth = width / 3
        val doorHeight = height / 2
        val doorX = x + (width - doorWidth) / 2
        val doorY = y + (height - doorHeight)
        canvas.drawRect(doorX.toFloat(), doorY.toFloat(), (doorX + doorWidth).toFloat(), (y + height).toFloat(), doorPaint)
    }
}
