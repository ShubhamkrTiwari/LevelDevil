package com.example.leveldevil20

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

enum class GameState {
    LEVEL_SELECTION,
    PLAYING,
    GAME_OVER,
    GAME_COMPLETED
}

class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private var thread: GameThread? = null
    private val player: Player
    private val levelManager: LevelManager
    private var score = 0
    private val textPaint = Paint()
    private val gameOverPaint = Paint()
    private val gameCompletedPaint = Paint()
    private val titlePaint = Paint()
    private val firePaint = Paint()
    private val pathPaint = Paint()
    private val iconPaint = Paint()

    private lateinit var leftButtonRect: Rect
    private lateinit var rightButtonRect: Rect
    private lateinit var jumpButtonRect: Rect
    private lateinit var nextButtonRect: Rect
    private lateinit var prevButtonRect: Rect

    private var gameState = GameState.LEVEL_SELECTION
    private var levelSelectionButtons = mutableListOf<LevelSelectionButton>()
    private var currentPage = 0
    private val levelsPerPage = 12

    private var leftPointerId = -1
    private var rightPointerId = -1

    init {
        holder.addCallback(this)
        player = Player(150, 400, 50)
        levelManager = LevelManager()

        textPaint.color = Color.BLACK
        textPaint.textSize = 40f

        iconPaint.color = Color.DKGRAY
        iconPaint.style = Paint.Style.STROKE
        iconPaint.strokeWidth = 10f

        titlePaint.color = Color.rgb(139, 0, 0)
        titlePaint.textSize = 120f
        titlePaint.textAlign = Paint.Align.CENTER

        firePaint.color = Color.rgb(139, 0, 0)
        firePaint.style = Paint.Style.FILL

        pathPaint.color = Color.BLACK
        pathPaint.style = Paint.Style.STROKE
        pathPaint.strokeWidth = 8f

        gameOverPaint.color = Color.RED
        gameOverPaint.textSize = 100f
        gameOverPaint.textAlign = Paint.Align.CENTER

        gameCompletedPaint.color = Color.GREEN
        gameCompletedPaint.textSize = 100f
        gameCompletedPaint.textAlign = Paint.Align.CENTER

        createLevelSelectionButtons()
    }

    private fun createLevelSelectionButtons() {
        levelSelectionButtons.clear()
        val buttonWidth = 40
        val buttonHeight = 40
        val positions = arrayOf(
            Pair(80, 300), Pair(200, 360), Pair(320, 300), Pair(440, 360), Pair(560, 300), Pair(680, 250),
            Pair(800, 300), Pair(920, 360), Pair(800, 420), Pair(680, 470), Pair(560, 420), Pair(440, 470)
        )

        val startLevel = currentPage * levelsPerPage
        for (i in 0 until levelsPerPage) {
            val levelIndex = startLevel + i
            if (levelIndex < levelManager.getLevelCount()) {
                val pos = positions[i]
                levelSelectionButtons.add(LevelSelectionButton(levelIndex, pos.first, pos.second, buttonWidth, buttonHeight))
            }
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        thread = GameThread(holder, this)
        thread?.setRunning(true)
        thread?.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        val buttonSize = 120
        val bottomMargin = 20
        leftButtonRect = Rect(100, height - buttonSize - bottomMargin, 100 + buttonSize, height - bottomMargin)
        rightButtonRect = Rect(250, height - buttonSize - bottomMargin, 250 + buttonSize, height - bottomMargin)
        jumpButtonRect = Rect(width - 220, height - buttonSize - bottomMargin, width - 100, height - bottomMargin)

        nextButtonRect = Rect(width - 200, height - 100, width - 50, height - 20)
        prevButtonRect = Rect(50, height - 100, 200, height - 20)
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
            val (coinsCollected, isGameOver) = player.handleCollision(
                level.getPlatforms(), level.coins, level.getEnemies(),
                level.getSpikes(), level.getHollowPlatforms(), level.getHouse()
            )
            score += coinsCollected

            if (isGameOver || player.y > height) {
                gameState = GameState.GAME_OVER
            }

            if (Rect.intersects(player.rect, level.getHouse().rect)) {
                if (!levelManager.nextLevel()) {
                    gameState = GameState.GAME_COMPLETED
                } else {
                    player.reset()
                }
            }
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        when (gameState) {
            GameState.LEVEL_SELECTION -> drawLevelSelectionScreen(canvas)
            GameState.PLAYING -> drawGame(canvas)
            GameState.GAME_OVER -> {
                drawGame(canvas)
                drawGameOver(canvas)
            }
            GameState.GAME_COMPLETED -> {
                drawGame(canvas)
                drawGameCompleted(canvas)
            }
        }
    }

    private fun drawLevelSelectionScreen(canvas: Canvas) {
        canvas.drawColor(Color.rgb(153, 153, 255)) // Light purple background
        drawPaths(canvas)
        for (button in levelSelectionButtons) {
            button.draw(canvas, button.levelIndex == levelManager.currentLevelIndex)
        }

        if (currentPage > 0) {
            canvas.drawText("Prev", 125f, height - 40f, textPaint)
        }
        if ((currentPage + 1) * levelsPerPage < levelManager.getLevelCount()) {
            canvas.drawText("Next", width - 125f, height - 40f, textPaint)
        }
    }

    private fun drawPaths(canvas: Canvas) {
        val path = Path()
        if (levelSelectionButtons.isNotEmpty()) {
            val firstButton = levelSelectionButtons.first()
            path.moveTo((firstButton.x + firstButton.width / 2).toFloat(), (firstButton.y + firstButton.height / 2).toFloat())
        }
        for (i in 0 until levelSelectionButtons.size - 1) {
            val start = levelSelectionButtons[i]
            val end = levelSelectionButtons[i + 1]
            val midX = (start.x + end.x) / 2 + (if (i % 2 == 0) 60 else -60)
            path.cubicTo(
                (start.x + start.width / 2).toFloat(), (start.y + start.height / 2).toFloat(),
                midX.toFloat(), ((start.y + end.y) / 2).toFloat(),
                (end.x + end.width / 2).toFloat(), (end.y + end.height / 2).toFloat()
            )
        }
        canvas.drawPath(path, pathPaint)
    }

    private fun drawGame(canvas: Canvas) {
        val level = levelManager.getCurrentLevel()
        canvas.drawColor(Color.rgb(153, 153, 255)) // Light purple background
        level.draw(canvas)
        player.draw(canvas)
        drawControlIcons(canvas)
    }

    private fun drawControlIcons(canvas: Canvas) {
        // Left arrow
        val leftPath = Path()
        leftPath.moveTo(leftButtonRect.exactCenterX() + 20, leftButtonRect.exactCenterY() - 30)
        leftPath.lineTo(leftButtonRect.exactCenterX() - 20, leftButtonRect.exactCenterY())
        leftPath.lineTo(leftButtonRect.exactCenterX() + 20, leftButtonRect.exactCenterY() + 30)
        canvas.drawPath(leftPath, iconPaint)

        // Right arrow
        val rightPath = Path()
        rightPath.moveTo(rightButtonRect.exactCenterX() - 20, rightButtonRect.exactCenterY() - 30)
        rightPath.lineTo(rightButtonRect.exactCenterX() + 20, rightButtonRect.exactCenterY())
        rightPath.lineTo(rightButtonRect.exactCenterX() - 20, rightButtonRect.exactCenterY() + 30)
        canvas.drawPath(rightPath, iconPaint)

        // Jump icon (up arrow)
        val jumpPath = Path()
        jumpPath.moveTo(jumpButtonRect.exactCenterX() - 30, jumpButtonRect.exactCenterY() + 20)
        jumpPath.lineTo(jumpButtonRect.exactCenterX(), jumpButtonRect.exactCenterY() - 20)
        jumpPath.lineTo(jumpButtonRect.exactCenterX() + 30, jumpButtonRect.exactCenterY() + 20)
        canvas.drawPath(jumpPath, iconPaint)
    }

    private fun drawGameOver(canvas: Canvas) {
        canvas.drawColor(Color.argb(150, 0, 0, 0))
        canvas.drawText("Game Over", (width / 2).toFloat(), (height / 2).toFloat(), gameOverPaint)
    }

    private fun drawGameCompleted(canvas: Canvas) {
        canvas.drawColor(Color.argb(150, 0, 0, 0))
        canvas.drawText("You Win!", (width / 2).toFloat(), (height / 2).toFloat(), gameCompletedPaint)
    }

    private fun restartGame() {
        gameState = GameState.LEVEL_SELECTION
        score = 0
        player.reset()
        levelManager.reset()
        leftPointerId = -1
        rightPointerId = -1
        createLevelSelectionButtons()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (gameState) {
            GameState.LEVEL_SELECTION -> {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val x = event.x.toInt()
                    val y = event.y.toInt()

                    for (button in levelSelectionButtons) {
                        if (button.rect.contains(x, y)) {
                            levelManager.setLevel(button.levelIndex)
                            gameState = GameState.PLAYING
                            return true
                        }
                    }

                    if (nextButtonRect.contains(x, y)) {
                        if ((currentPage + 1) * levelsPerPage < levelManager.getLevelCount()) {
                            currentPage++
                            createLevelSelectionButtons()
                        }
                    }

                    if (prevButtonRect.contains(x, y)) {
                        if (currentPage > 0) {
                            currentPage--
                            createLevelSelectionButtons()
                        }
                    }
                }
            }
            GameState.PLAYING -> {
                val action = event.actionMasked
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
            }
            GameState.GAME_OVER, GameState.GAME_COMPLETED -> {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    restartGame()
                }
            }
        }
        return true
    }
}
