package com.example.leveldevil20

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

open class ControlButton(var x: Int, var y: Int, var width: Int, var height: Int, private val label: String) {

    private val paint = Paint().apply {
        color = Color.DKGRAY
    }
    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }
    val rect: Rect
        get() = Rect(x, y, x + width, y + height)

    fun draw(canvas: Canvas) {
        canvas.drawRect(rect, paint)
        canvas.drawText(label, (x + width / 2).toFloat(), (y + height / 2 + 15).toFloat(), textPaint)
    }
}
