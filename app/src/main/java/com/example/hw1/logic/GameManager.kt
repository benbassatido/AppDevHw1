package com.example.hw1

import android.view.View
import android.widget.ImageView
import com.example.hw1.utilities.Timer

class GameManager(
    private val laneCars: Array<ImageView>,
    private val nails: Array<ImageView>,
    private val hearts: Array<ImageView>,
) {

    var onCarHitListener: (() -> Unit)? = null

    companion object {
        private const val ROWS = 6
        private const val COLS = 3

        private const val TICK_DELAY = 800L
        private const val START_LANE = 1

        private const val MAX_NAIL_INDEX = 11

        private const val INDEX_COUNT = 2

        private const val MAX_LIVES = 3
    }

    private var isGameOver = false

    var currentLane: Int = START_LANE
        private set

    private var lives: Int = MAX_LIVES

    private var tickCount = 0

    private val timer = Timer(TICK_DELAY) {
        gameTick()
    }


    private fun indexOf(row: Int, col: Int): Int = row * COLS + col


    fun startGame() {
        isGameOver = false
        currentLane = START_LANE
        lives = MAX_LIVES
        tickCount = 0
        updateCarUi()
        updateHeartsUi()
        spawnRandomTwoNails(INDEX_COUNT)
        timer.start()
    }

    fun stopGame() {
        isGameOver = true
        timer.stop()
    }


    fun moveCarLeft() {
        if (isGameOver) return
        if (currentLane > 0) {
            currentLane--
            updateCarUi()
        }
    }

    fun moveCarRight() {
        if (isGameOver) return
        if (currentLane < laneCars.size - 1) {
            currentLane++
            updateCarUi()
        }
    }

    private fun updateCarUi() {
        for (i in laneCars.indices) {
            laneCars[i].visibility =
                if (i == currentLane) View.VISIBLE else View.INVISIBLE
        }
    }


    private fun gameTick() {
        if (isGameOver) return
        tickCount++
        handleBottomRow()
        stepNailsDown()

        if (tickCount % 2 == 0) {
            spawnNailInTopRow()
        }
    }


    private fun clearBoard() {
        for (nail in nails) {
            nail.visibility = View.INVISIBLE
        }
    }

    private fun spawnRandomTwoNails(count: Int) {
        clearBoard()

        val allowedIndexes = (0..MAX_NAIL_INDEX).shuffled().take(count)
        for (index in allowedIndexes) {
            nails[index].visibility = View.VISIBLE
        }
    }

    private fun spawnNailInTopRow() {
        val emptyCols = (0 until COLS).filter { col ->
            val index = indexOf(0, col)
            nails[index].visibility == View.INVISIBLE
        }

        if (emptyCols.isNotEmpty()) {
            val chosenCol = emptyCols.random()
            val index = indexOf(0, chosenCol)
            nails[index].visibility = View.VISIBLE
        }
    }

    private fun stepNailsDown() {

        for (row in ROWS - 2 downTo 0) {
            for (col in 0 until COLS) {

                val index = indexOf(row, col)
                if (nails[index].visibility != View.VISIBLE)
                    continue

                val newRow = row + 1
                val newIndex = indexOf(newRow, col)

                if (newRow == ROWS - 1 && col == currentLane) {
                    nails[index].visibility = View.INVISIBLE
                    onCarHit()
                    continue
                }

                nails[index].visibility = View.INVISIBLE
                nails[newIndex].visibility = View.VISIBLE
            }
        }
    }

    private fun handleBottomRow() {
        val bottomRow = ROWS - 1

        for (col in 0 until COLS) {
            val index = indexOf(bottomRow, col)

            if (nails[index].visibility == View.VISIBLE) {
                nails[index].visibility = View.INVISIBLE
            }
        }
    }


    private fun onCarHit() {
        if (lives > 0) {
            lives--
            updateHeartsUi()
        }

        onCarHitListener?.invoke()

        if (lives <= 0) {
            stopGame()
        }
    }

    private fun updateHeartsUi() {
        for (i in hearts.indices) {
            val index = hearts.size - 1 - i
            hearts[index].visibility =
                if (i < lives) View.VISIBLE else View.INVISIBLE
        }
    }
}
