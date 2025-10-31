package com.example.leveldevil20

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class Obstacle(var x: Int, var y: Int, var width: Int, var height: Int) {

    private val paint = Paint().apply {
        color = Color.RED
    }
    var velocityY = 10
    val rect: Rect
        get() = Rect(x, y, x + width, y + height)

    fun draw(canvas: Canvas) {
        canvas.drawRect(rect, paint)
    }

}
