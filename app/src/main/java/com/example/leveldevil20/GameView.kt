package com.example.leveldevil20

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private var thread: GameThread? = null
    private val player: Player
    private val levelManager: LevelManager
    private var score = 0
    private val textPaint = Paint()
    private val scrollSpeed = 5
    private val jumpButton: JumpButton
    private val leftButton: MoveButton
    private val rightButton: MoveButton

    init {
        holder.addCallback(this)
        player = Player(150, 550, 50) // Adjust player starting position
        levelManager = LevelManager()
        jumpButton = JumpButton(1300, 600, 200, 100)
        leftButton = MoveButton(50, 600, 200, 100, "Left")
        rightButton = MoveButton(300, 600, 200, 100, "Right")

        textPaint.color = Color.BLACK
        textPaint.textSize = 40f
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        thread = GameThread(holder, this)
        thread?.setRunning(true)
        thread?.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        thread?.setRunning(false)
        while (retry) {
            try {
                thread?.join()
                retry = false
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    fun update() {
        player.update()
        val level = levelManager.getCurrentLevel()
        level.update(scrollSpeed)
        val coinsCollected = player.handleCollision(
            level.getPlatforms(),
            level.coins,
            level.getEnemies(),
            level.getSpikes()
        )
        score += coinsCollected

        // Check if level is complete
        if (level.coins.isEmpty()) {
            levelManager.nextLevel()
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        val level = levelManager.getCurrentLevel()

        canvas.drawColor(Color.rgb(222, 184, 135)) // Sand color
        level.draw(canvas)
        player.draw(canvas)
        jumpButton.draw(canvas)
        leftButton.draw(canvas)
        rightButton.draw(canvas)

        // Draw UI elements
        canvas.drawText("Level: ${levelManager.currentLevelIndex + 1}", 50f, 50f, textPaint)
        canvas.drawText("Score: $score", (width - 250).toFloat(), 50f, textPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (jumpButton.rect.contains(event.x.toInt(), event.y.toInt())) {
                    player.jump()
                }
                if (leftButton.rect.contains(event.x.toInt(), event.y.toInt())) {
                    player.moveLeft()
                }
                if (rightButton.rect.contains(event.x.toInt(), event.y.toInt())) {
                    player.moveRight()
                }
            }
            MotionEvent.ACTION_UP -> {
                player.stopMoving()
            }
        }
        return true
    }
}