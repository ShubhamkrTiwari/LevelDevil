package com.example.leveldevil20

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect

class LevelSelectionButton(val levelIndex: Int, var x: Int, var y: Int, var width: Int, var height: Int) {

    private val nodePaint = Paint().apply {
        color = Color.rgb(139, 90, 43)
        style = Paint.Style.FILL
    }

    private val borderPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    private val facePaint = Paint().apply {
        color = Color.BLACK
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 20f
        textAlign = Paint.Align.CENTER
    }

    private val selectionMarkerPaint = Paint().apply {
        color = Color.YELLOW
        style = Paint.Style.FILL
    }

    val rect: Rect
        get() = Rect(x, y, x + width, y + height)

    fun draw(canvas: Canvas, isCurrent: Boolean) {
        val nodePath = Path()
        nodePath.moveTo(x.toFloat(), (y + height).toFloat())
        nodePath.quadTo(x.toFloat(), y.toFloat(), (x + width / 2).toFloat(), y.toFloat())
        nodePath.quadTo((x + width).toFloat(), y.toFloat(), (x + width).toFloat(), (y + height).toFloat())
        nodePath.close()

        canvas.drawPath(nodePath, nodePaint)
        canvas.drawPath(nodePath, borderPaint)

        // Devil Face (smaller and shifted up)
        facePaint.style = Paint.Style.FILL
        val eyeRadius = 3f
        canvas.drawCircle(x + width * 0.35f, y + height * 0.3f, eyeRadius, facePaint)
        canvas.drawCircle(x + width * 0.65f, y + height * 0.3f, eyeRadius, facePaint)

        facePaint.style = Paint.Style.STROKE
        facePaint.strokeWidth = 3f
        val smilePath = Path()
        smilePath.moveTo(x + width * 0.35f, y + height * 0.5f)
        smilePath.quadTo(x + width * 0.5f, y + height * 0.6f, x + width * 0.65f, y + height * 0.5f)
        canvas.drawPath(smilePath, facePaint)

        // Level Number
        canvas.drawText((levelIndex + 1).toString(), (x + width / 2).toFloat(), (y + height * 0.85f), textPaint)

        if (isCurrent) {
            val markerPath = Path()
            val markerX = x.toFloat() - 20
            val markerY = y.toFloat() + height / 2
            markerPath.moveTo(markerX, markerY - 10)
            markerPath.lineTo(x.toFloat(), markerY)
            markerPath.lineTo(markerX, markerY + 10)
            markerPath.close()
            canvas.drawPath(markerPath, selectionMarkerPaint)
        }
    }
}