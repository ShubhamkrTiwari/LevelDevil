package com.example.leveldevil20

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class Platform(var x: Int, var y: Int, var width: Int, var height: Int) {
    private val grassPaint = Paint().apply {
        color = Color.rgb(0, 154, 23) // A vibrant green
    }
    private val topSoilPaint = Paint().apply {
        color = Color.rgb(92, 64, 51) // A dark brown for the top soil layer
    }
    private val mainPlatformPaint = Paint().apply {
        color = Color.rgb(160, 82, 45) // Sienna
    }

    val rect: Rect
        get() = Rect(x, y, x + width, y + height)

    fun draw(canvas: Canvas) {
        val grassHeight = 15
        val topSoilHeight = 20

        // Main platform body
        canvas.drawRect(
            x.toFloat(),
            (y + grassHeight + topSoilHeight).toFloat(),
            (x + width).toFloat(),
            (y + height).toFloat(),
            mainPlatformPaint
        )

        // Top soil layer
        canvas.drawRect(
            x.toFloat(),
            (y + grassHeight).toFloat(),
            (x + width).toFloat(),
            (y + grassHeight + topSoilHeight).toFloat(),
            topSoilPaint
        )

        // Grass on top
        canvas.drawRect(
            x.toFloat(),
            y.toFloat(),
            (x + width).toFloat(),
            (y + grassHeight).toFloat(),
            grassPaint
        )
    }
}