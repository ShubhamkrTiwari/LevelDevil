package com.example.leveldevil20

import android.graphics.Canvas

class Level(
    private val platforms: List<Platform>,
    private val spikes: List<Spike>,
    private val suddenSpikes: List<SuddenSpike>,
    private val door: Door?
) {

    fun draw(canvas: Canvas) {
        for (platform in platforms) {
            platform.draw(canvas)
        }
        for (spike in spikes) {
            spike.draw(canvas)
        }
        for (spike in suddenSpikes) {
            spike.draw(canvas)
        }
        door?.draw(canvas)
    }

    fun update() {
        for (spike in suddenSpikes) {
            spike.update()
        }
    }

    fun getPlatforms(): List<Platform> {
        return platforms
    }

    fun getSpikes(): List<Spike> {
        return spikes
    }

    fun getSuddenSpikes(): List<SuddenSpike> {
        return suddenSpikes
    }

    fun getDoor(): Door? {
        return door
    }
}