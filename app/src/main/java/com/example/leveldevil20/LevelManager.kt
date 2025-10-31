package com.example.leveldevil20

class LevelManager {
    private val levels = mutableListOf<Level>()
    var currentLevelIndex = 0
        private set

    init {
        // Create and add levels
        val platforms = listOf(
            Platform(0, 600, 400, 100), // Left part of the ground
            Platform(600, 600, 400, 100)  // Right part of the ground
        )
        val hollowPlatforms = listOf(
            HollowPlatform(400, 600, 200, 100) // Hollow part of the ground
        )
        val coins = mutableListOf<Coin>()
        val enemies = listOf<Enemy>()
        val spikes = listOf(
            Spike(400, 700, 200, 50) // Spikes under the hollow platform
        )
        levels.add(Level(platforms, coins, enemies, spikes, hollowPlatforms))
    }

    fun getCurrentLevel(): Level {
        return levels[currentLevelIndex]
    }

    fun nextLevel() {
        if (currentLevelIndex < levels.size - 1) {
            currentLevelIndex++
        }
    }

    fun resetCurrentLevel() {
        // This will need to be implemented to reset the coins and enemies
    }
}