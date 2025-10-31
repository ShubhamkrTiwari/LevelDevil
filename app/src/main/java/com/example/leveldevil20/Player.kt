package com.example.leveldevil20

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class Player(var x: Int, var y: Int, var size: Int) {
    private val startX = x
    private val startY = y
    private val bodyPaint = Paint()
    private val headPaint = Paint()
    private val eyePaint = Paint()
    private var velocityY = 0
    private var velocityX = 0 // Added for horizontal movement
    private val moveSpeed = 10 // Speed of horizontal movement
    private val gravity = 2
    private val jumpForce = -30
    private var isJumping = false

    val rect: Rect
        get() = Rect(x, y, x + size, y + size)

    init {
        bodyPaint.color = Color.rgb(0, 150, 255) // Blue body
        headPaint.color = Color.rgb(255, 200, 0) // Yellow head
        eyePaint.color = Color.BLACK
    }

    fun draw(canvas: Canvas) {
        // Draw body
        canvas.drawRect(rect, bodyPaint)

        // Draw head (a circle)
        val headRadius = size / 2
        canvas.drawCircle((x + size / 2).toFloat(), (y - headRadius).toFloat(), headRadius.toFloat(), headPaint)

        // Draw eye
        val eyeRadius = headRadius / 4
        canvas.drawCircle((x + size / 2 + headRadius / 2).toFloat(), (y - headRadius).toFloat(), eyeRadius.toFloat(), eyePaint)
    }

    fun jump() {
        if (!isJumping) {
            velocityY = jumpForce
            isJumping = true
        }
    }

    // New methods for horizontal movement
    fun moveLeft() {
        velocityX = -moveSpeed
    }

    fun moveRight() {
        velocityX = moveSpeed
    }

    fun stopMoving() {
        velocityX = 0
    }


    fun update() {
        // Horizontal movement
        x += velocityX

        // Vertical movement
        velocityY += gravity
        y += velocityY
    }

    fun handleCollision(platforms: List<Platform>, coins: MutableList<Coin>, enemies: List<Enemy>, spikes: List<Spike>): Int {
        var coinsCollected = 0
        // Platform collision
        for (platform in platforms) {
            if (Rect.intersects(rect, platform.rect)) {
                if (velocityY > 0) {
                    y = platform.rect.top - size
                    velocityY = 0
                    isJumping = false
                }
            }
        }

        // Coin collision
        val coinIterator = coins.iterator()
        while (coinIterator.hasNext()) {
            val coin = coinIterator.next()
            if (Rect.intersects(rect, coin.rect)) {
                coinIterator.remove()
                coinsCollected++
            }
        }

        // Enemy collision
        for (enemy in enemies) {
            if (Rect.intersects(rect, enemy.rect)) {
                // Reset player position
                x = startX
                y = startY
                velocityY = 0
            }
        }

        // Spike collision
        for (spike in spikes) {
            if (Rect.intersects(rect, spike.rect)) {
                // Reset player position
                x = startX
                y = startY
                velocityY = 0
            }
        }
        return coinsCollected
    }
}