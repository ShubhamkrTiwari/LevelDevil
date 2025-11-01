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
            val suddenSpikes = mutableListOf<SuddenSpike>()

            // Base room structure
            val roomWidth = 800 + i * 10
            val roomHeight = 300 + i * 5
            val roomX = 100
            val roomY = 200

            platforms.add(Platform(roomX, roomY, roomWidth, roomHeight)) // Main playable area

            // Add internal walls and passages that get more complex with each level
            val wallCount = i / 5
            for (j in 0..wallCount) {
                val wallX = roomX + 100 + random.nextInt(roomWidth - 200)
                val wallY = roomY + random.nextInt(roomHeight - 100)
                val wallWidth = 50 + random.nextInt(100)
                val wallHeight = 50 + random.nextInt(50)
                platforms.add(Platform(wallX, wallY, wallWidth, wallHeight))
            }

            // Add spikes with increasing difficulty
            if (i > 2) {
                for (k in 0 until i / 3) {
                    val spikeX = roomX + 50 + random.nextInt(roomWidth - 100)
                    val spikeY = roomY + roomHeight - 20 // Place spikes on the floor
                    spikes.add(Spike(spikeX, spikeY, 20, 20))
                }
            }
            if(i > 5) {
                 for (k in 0 until i / 6) {
                    val spikeX = roomX + 50 + random.nextInt(roomWidth - 100)
                    suddenSpikes.add(SuddenSpike(spikeX, roomY, 20, 20, 30, 30)) // Place sudden spikes on the ceiling
                }
            }

            val door = Door(roomX + roomWidth - 70, roomY + roomHeight - 70, 50, 50)
            loadedLevels.add(Level(platforms, spikes, suddenSpikes, door))
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