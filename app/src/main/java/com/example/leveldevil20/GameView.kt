package com.example.leveldevil20

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

enum class GameState {
    PLAYING,
    GAME_OVER,
    GAME_COMPLETED
}

class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private var thread: GameThread? = null
    private val player: Player
    private val levelManager: LevelManager
    private val gameOverPaint = Paint()
    private val gameCompletedPaint = Paint()
    private val uiPaint = Paint()

    // UI Rects
    private lateinit var backButtonRect: Rect
    private lateinit var resetButtonRect: Rect
    private lateinit var leftButtonRect: Rect
    private lateinit var rightButtonRect: Rect
    private lateinit var jumpButtonRect: Rect

    private var gameState = GameState.PLAYING
    private var lives = 5

    private var leftPointerId = -1
    private var rightPointerId = -1

    init {
        holder.addCallback(this)
        player = Player(150, 450, 25)
        levelManager = LevelManager()

        gameOverPaint.color = Color.WHITE
        gameOverPaint.textSize = 80f
        gameOverPaint.textAlign = Paint.Align.CENTER

        gameCompletedPaint.color = Color.GREEN
        gameCompletedPaint.textSize = 80f
        gameCompletedPaint.textAlign = Paint.Align.CENTER

        uiPaint.color = Color.BLACK
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        thread = GameThread(holder, this)
        thread?.setRunning(true)
        thread?.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // Top UI buttons
        backButtonRect = Rect(20, 20, 100, 100)
        resetButtonRect = Rect(120, 20, 200, 100)

        // Bottom control buttons
        val buttonWidth = 180
        val buttonHeight = 100
        val bottomMargin = 20
        leftButtonRect = Rect(50, height - buttonHeight - bottomMargin, 50 + buttonWidth, height - bottomMargin)
        rightButtonRect = Rect(250, height - buttonHeight - bottomMargin, 250 + buttonWidth, height - bottomMargin)
        jumpButtonRect = Rect(width - 50 - buttonWidth, height - buttonHeight - bottomMargin, width - 50, height - bottomMargin)
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
        if (gameState == GameState.PLAYING) {
            player.update()
            val level = levelManager.getCurrentLevel()
            level.update()
            val isGameOver = player.handleCollision(level.getPlatforms(), level.getSpikes(), level.getSuddenSpikes(), level.getDoor())

            if (isGameOver || player.y > height) {
                lives--
                if (lives <= 0) {
                    gameState = GameState.GAME_OVER
                } else {
                    player.reset()
                }
            }

            level.getDoor()?.let {
                if (Rect.intersects(player.rect, it.rect)) {
                    if (!levelManager.nextLevel()) {
                        gameState = GameState.GAME_COMPLETED
                    } else {
                        player.reset()
                    }
                }
            }
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        when (gameState) {
            GameState.PLAYING -> drawGame(canvas)
            GameState.GAME_OVER -> drawGameOver(canvas)
            GameState.GAME_COMPLETED -> drawGameCompleted(canvas)
        }
    }

    private fun drawGame(canvas: Canvas) {
        canvas.drawColor(Color.rgb(205, 92, 92)) // Reddish-orange background
        val level = levelManager.getCurrentLevel()
        level.draw(canvas)
        player.draw(canvas)
        drawTopUI(canvas)
        drawBottomControls(canvas)
    }

    private fun drawTopUI(canvas: Canvas) {
        // Back Button
        uiPaint.color = Color.rgb(139, 0, 0)
        canvas.drawRect(backButtonRect, uiPaint)
        uiPaint.color = Color.WHITE
        val backPath = Path()
        backPath.moveTo(backButtonRect.exactCenterX() + 15, backButtonRect.exactCenterY() - 25)
        backPath.lineTo(backButtonRect.exactCenterX() - 15, backButtonRect.exactCenterY())
        backPath.lineTo(backButtonRect.exactCenterX() + 15, backButtonRect.exactCenterY() + 25)
        uiPaint.style = Paint.Style.STROKE
        uiPaint.strokeWidth = 8f
        canvas.drawPath(backPath, uiPaint)

        // Reset Button
        uiPaint.color = Color.rgb(139, 0, 0)
        uiPaint.style = Paint.Style.FILL
        canvas.drawRect(resetButtonRect, uiPaint)
        uiPaint.color = Color.WHITE
        uiPaint.strokeWidth = 8f
        uiPaint.style = Paint.Style.STROKE
        canvas.drawArc(RectF((resetButtonRect.left + 20).toFloat(),
            (resetButtonRect.top + 20).toFloat(),
            (resetButtonRect.right - 20).toFloat(), (resetButtonRect.bottom - 20).toFloat()
        ), 0f, 270f, false, uiPaint)
        val resetArrow = Path()
        resetArrow.moveTo(resetButtonRect.right - 20f, resetButtonRect.centerY().toFloat())
        resetArrow.lineTo(resetButtonRect.right - 35f, resetButtonRect.centerY() - 15f)
        resetArrow.lineTo(resetButtonRect.right - 35f, resetButtonRect.centerY() + 15f)
        resetArrow.close()
        uiPaint.style = Paint.Style.FILL
        canvas.drawPath(resetArrow, uiPaint)


        // Lives Counter
        uiPaint.color = Color.BLACK
        val boxSize = 30
        val spacing = 10
        val startX = width / 2f - (5 * (boxSize + spacing)) / 2f
        for (i in 0 until 5) {
            if(i < lives) {
                uiPaint.style = Paint.Style.FILL
            } else {
                uiPaint.style = Paint.Style.STROKE
            }
            canvas.drawRect(startX + i * (boxSize + spacing), 40f, startX + i * (boxSize + spacing) + boxSize, 40f + boxSize, uiPaint)
        }
    }

    private fun drawBottomControls(canvas: Canvas) {
        uiPaint.color = Color.BLACK
        uiPaint.style = Paint.Style.STROKE
        uiPaint.strokeWidth = 6f

        // Left Button Shape
        val leftPath = Path()
        leftPath.moveTo(leftButtonRect.left + 30f, leftButtonRect.top.toFloat())
        leftPath.lineTo(leftButtonRect.right.toFloat(), leftButtonRect.top.toFloat())
        leftPath.lineTo(leftButtonRect.right.toFloat(), leftButtonRect.bottom.toFloat())
        leftPath.lineTo(leftButtonRect.left + 30f, leftButtonRect.bottom.toFloat())
        leftPath.lineTo(leftButtonRect.left.toFloat(), leftButtonRect.centerY().toFloat())
        leftPath.close()
        canvas.drawPath(leftPath, uiPaint)

        // Right Button Shape
        val rightPath = Path()
        rightPath.moveTo(rightButtonRect.left.toFloat(), rightButtonRect.top.toFloat())
        rightPath.lineTo(rightButtonRect.right - 30f, rightButtonRect.top.toFloat())
        rightPath.lineTo(rightButtonRect.right.toFloat(), rightButtonRect.centerY().toFloat())
        rightPath.lineTo(rightButtonRect.right - 30f, rightButtonRect.bottom.toFloat())
        rightPath.lineTo(rightButtonRect.left.toFloat(), rightButtonRect.bottom.toFloat())
        rightPath.close()
        canvas.drawPath(rightPath, uiPaint)

        // Jump Button Shape
        val jumpPath = Path()
        jumpPath.moveTo(jumpButtonRect.left.toFloat(), jumpButtonRect.bottom.toFloat())
        jumpPath.lineTo(jumpButtonRect.left.toFloat(), jumpButtonRect.top + 30f)
        jumpPath.lineTo(jumpButtonRect.centerX().toFloat(), jumpButtonRect.top.toFloat())
        jumpPath.lineTo(jumpButtonRect.right.toFloat(), jumpButtonRect.top + 30f)
        jumpPath.lineTo(jumpButtonRect.right.toFloat(), jumpButtonRect.bottom.toFloat())
        jumpPath.close()
        canvas.drawPath(jumpPath, uiPaint)
    }

    private fun drawGameOver(canvas: Canvas) {
        drawGame(canvas)
        canvas.drawColor(Color.argb(150, 0, 0, 0))
        canvas.drawText("Game Over", (width / 2).toFloat(), (height / 2).toFloat(), gameOverPaint)
    }

    private fun drawGameCompleted(canvas: Canvas) {
        drawGame(canvas)
        canvas.drawColor(Color.argb(150, 0, 0, 0))
        canvas.drawText("You Win!", (width / 2).toFloat(), (height / 2).toFloat(), gameCompletedPaint)
    }

    private fun restartGame() {
        lives = 5
        gameState = GameState.PLAYING
        player.reset()
        levelManager.reset()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.actionMasked
        if (gameState == GameState.PLAYING) {
            for (i in 0 until event.pointerCount) {
                val pointerId = event.getPointerId(i)
                val x = event.getX(i).toInt()
                val y = event.getY(i).toInt()

                when (action) {
                    MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                        if (jumpButtonRect.contains(x, y)) player.jump()
                        if (leftButtonRect.contains(x, y)) {
                            leftPointerId = pointerId
                            player.moveLeft()
                        }
                        if (rightButtonRect.contains(x, y)) {
                            rightPointerId = pointerId
                            player.moveRight()
                        }
                        if (resetButtonRect.contains(x, y)) restartGame()
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                        if (pointerId == leftPointerId) {
                            leftPointerId = -1
                            player.stopMoving()
                        } else if (pointerId == rightPointerId) {
                            rightPointerId = -1
                            player.stopMoving()
                        }
                    }
                }
            }
        } else {
            if (action == MotionEvent.ACTION_DOWN) {
                restartGame()
            }
        }
        return true
    }
}