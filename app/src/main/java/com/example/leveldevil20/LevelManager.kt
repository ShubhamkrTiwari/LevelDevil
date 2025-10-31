package com.example.leveldevil20

import java.util.Random

class LevelManager {
    private var levels = mutableListOf<Level>()
    var currentLevelIndex = 0
        private set

    init {
        levels = loadLevels()
    }

    private fun loadLevels(): MutableList<Level> {
        val loadedLevels = mutableListOf<Level>()
        val random = Random()

        for (i in 0 until 50) {
            val platforms = mutableListOf<Platform>()
            val spikes = mutableListOf<Spike>()
            var currentX = 0
            var lastY = 500

            // Start platform
            platforms.add(Platform(currentX, lastY, 200, 100))
            currentX += 200

            val platformCount = 2 + i / 8 // Increase platform count more quickly
            for (j in 0 until platformCount) {
                val gap = 80 + random.nextInt(100 + i * 3) // Wider and more varied gaps
                currentX += gap

                val yChange = random.nextInt(100 + i * 2) - (50 + i) // More aggressive vertical changes
                var newY = lastY + yChange
                if (newY > 550) newY = 550 // Platforms can go a bit lower
                if (newY < 300) newY = 300 // And a bit higher
                lastY = newY

                val width = Math.max(80, 250 - i * 4) // Platforms can get narrower
                platforms.add(Platform(currentX, newY, width, 100))

                // Add spikes with increasing frequency and more varied placement
                if (i > 3 && random.nextInt(100) < i * 2.5) {
                    val spikeX = currentX + random.nextInt(Math.max(1, width - 50))
                    spikes.add(Spike(spikeX, newY - 50, 50, 50))
                }

                currentX += width
            }

            val lastPlatform = platforms.last()
            val house = House(lastPlatform.x + lastPlatform.width / 2 - 50, lastPlatform.y - 100, 100, 100)
            loadedLevels.add(Level(platforms, mutableListOf(), listOf(), spikes, listOf(), house))
        }

        return loadedLevels
    }

    fun getLevelCount(): Int {
        return levels.size
    }

    fun getCurrentLevel(): Level {
        return levels[currentLevelIndex]
    }

    fun setLevel(levelIndex: Int) {
        if (levelIndex >= 0 && levelIndex < levels.size) {
            currentLevelIndex = levelIndex
        }
    }

    fun nextLevel(): Boolean {
        if (currentLevelIndex < levels.size - 1) {
            currentLevelIndex++
            return true
        }
        return false
    }

    fun reset() {
        currentLevelIndex = 0
    }
}