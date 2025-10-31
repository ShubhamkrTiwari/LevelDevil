package com.example.leveldevil20

import android.graphics.Canvas

class Level(
    private val platforms: List<Platform>,
    val coins: MutableList<Coin>,
    private val enemies: List<Enemy>,
    private val spikes: List<Spike>,
    private val hollowPlatforms: List<HollowPlatform>
) {

    fun draw(canvas: Canvas) {
        for (platform in platforms) {
            platform.draw(canvas)
        }
        for (hollowPlatform in hollowPlatforms) {
            hollowPlatform.draw(canvas)
        }
        for (coin in coins) {
            coin.draw(canvas)
        }
        for (enemy in enemies) {
            enemy.draw(canvas)
        }
        for (spike in spikes) {
            spike.draw(canvas)
        }
    }

    fun update(scrollSpeed: Int) {
        for (p in platforms) {
            p.x -= scrollSpeed
        }
        for (hp in hollowPlatforms) {
            hp.x -= scrollSpeed
        }
        for (c in coins) {
            c.x -= scrollSpeed
        }
        for (e in enemies) {
            e.x -= scrollSpeed
        }
        for (s in spikes) {
            s.x -= scrollSpeed
        }
    }

    fun getPlatforms(): List<Platform> {
        return platforms
    }

    fun getEnemies(): List<Enemy> {
        return enemies
    }

    fun getSpikes(): List<Spike> {
        return spikes
    }
}