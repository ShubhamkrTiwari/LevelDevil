package com.example.leveldevil20

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class Player(var x: Int, var y: Int, var size: Int) {
    private val startX = x
    private val startY = y
    private val paint = Paint().apply {
        color = Color.BLACK
    }
    private var velocityY = 0
    private var velocityX = 0
    private val moveSpeed = 10
    private val gravity = 2
    private val jumpForce = -30
    private var isJumping = false
    private var walkCycle = 0

    val rect: Rect
        get() = Rect(x, y, x + size, y + size)

    fun draw(canvas: Canvas) {
        val totalHeight = size * 1.5f
        val headRatio = 0.25f
        val legRatio = 0.4f

        val headHeight = totalHeight * headRatio
        val bodyHeight = totalHeight * (1 - headRatio - legRatio)
        val legHeight = totalHeight * legRatio

        val headWidth = size * 0.7f
        val bodyWidth = size.toFloat()
        val legWidth = size * 0.45f

        val adjustedY = y - (totalHeight - size)

        // Head
        val headX = x + (size - headWidth) / 2
        canvas.drawRect(headX, adjustedY, headX + headWidth, adjustedY + headHeight, paint)

        // Body
        val bodyY = adjustedY + headHeight
        canvas.drawRect(x.toFloat(), bodyY, x + bodyWidth, bodyY + bodyHeight, paint)

        // Legs
        val legY = bodyY + bodyHeight
        val isMoving = velocityX != 0

        if (isMoving) {
            val legMovement = (Math.sin(walkCycle * (Math.PI / 10)) * size * 0.2f).toFloat()
            canvas.drawRect(x.toFloat(), legY, x + legWidth, legY + legHeight - legMovement, paint)
            canvas.drawRect(x + bodyWidth - legWidth, legY, x + bodyWidth, legY + legHeight + legMovement, paint)
        } else {
            canvas.drawRect(x.toFloat(), legY, x + legWidth, legY + legHeight, paint)
            canvas.drawRect(x + bodyWidth - legWidth, legY, x + bodyWidth, legY + legHeight, paint)
        }
    }

    fun jump() {
        if (!isJumping) {
            velocityY = jumpForce
            isJumping = true
        }
    }

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
        x += velocityX
        velocityY += gravity
        y += velocityY

        if (velocityX != 0) {
            walkCycle = (walkCycle + 1) % 20
        } else {
            walkCycle = 0
        }
    }

    fun reset() {
        x = startX
        y = startY
        velocityY = 0
        velocityX = 0
    }

    fun handleCollision(
        platforms: List<Platform>,
        coins: MutableList<Coin>,
        enemies: List<Enemy>,
        spikes: List<Spike>,
        hollowPlatforms: List<HollowPlatform>,
        house: House
    ): Pair<Int, Boolean> {
        var coinsCollected = 0
        var isGameOver = false

        for (platform in platforms) {
            if (rect.intersect(platform.rect)) {
                if (velocityY > 0 && rect.bottom > platform.rect.top) {
                    y = platform.rect.top - size
                    velocityY = 0
                    isJumping = false
                }
            }
        }

        for (hollowPlatform in hollowPlatforms) {
            if (rect.intersect(hollowPlatform.rect)) {
                isGameOver = true
            }
        }

        val coinIterator = coins.iterator()
        while (coinIterator.hasNext()) {
            if (rect.intersect(coinIterator.next().rect)) {
                coinIterator.remove()
                coinsCollected++
            }
        }

        for (enemy in enemies) {
            if (rect.intersect(enemy.rect)) {
                isGameOver = true
            }
        }

        for (spike in spikes) {
            if (rect.intersect(spike.rect)) {
                isGameOver = true
            }
        }

        return Pair(coinsCollected, isGameOver)
    }
}