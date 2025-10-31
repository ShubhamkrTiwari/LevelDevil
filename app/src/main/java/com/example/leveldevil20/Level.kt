package com.example.leveldevil20

import android.graphics.Canvas

class Level(
    private val platforms: List<Platform>,
    val coins: MutableList<Coin>,
    private val enemies: List<Enemy>,
    private val spikes: List<Spike>,
    private val hollowPlatforms: List<HollowPlatform>,
    private val house: House
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
        house.draw(canvas)
    }

    fun update() {
        // No-op
    }

    fun getPlatforms(): List<Platform> {
        return platforms
    }

    fun getHollowPlatforms(): List<HollowPlatform> {
        return hollowPlatforms
    }

    fun getEnemies(): List<Enemy> {
        return enemies
    }

    fun getSpikes(): List<Spike> {
        return spikes
    }

    fun getHouse(): House {
        return house
    }
}