package com.example.leveldevil20

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val frameLayout = FrameLayout(this)
        val gameView = GameView(this)

        val layoutParams = FrameLayout.LayoutParams(
            1280, // width in pixels
            720,  // height in pixels
            Gravity.CENTER
        )

        frameLayout.addView(gameView, layoutParams)
        setContentView(frameLayout)
    }
}