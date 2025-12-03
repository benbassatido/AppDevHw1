package com.example.hw1

import android.view.View
import android.widget.ImageView
import android.widget.Toast
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
    private val board = Array(ROWS){ BooleanArray(COLS) }
    var currentLane: Int = START_LANE
        private set

    private var lives: Int = MAX_LIVES


    private val timer = Timer(TICK_DELAY) {
        gameTick()
    }

    fun startGame() {
        currentLane = START_LANE
        lives = MAX_LIVES
        updateCarUi()
        updateHeartsUi()
        clearBoard()
        spawnRandomTwoNails(INDEX_COUNT)
        updateNailsUi()
        timer.start()
    }

    fun stopGame(){
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

    fun gameTick() {
        handleBottomRow()
        stepNailsDown()
        updateNailsUi()
    }


    private fun clearBoard() {
        for (r in 0 until ROWS)
            for (c in 0 until COLS)
                board[r][c] = false
    }
    private fun clearBottomRow() {
        for (col in 0 until COLS) {
            board[ROWS - 1][col] = false
        }
    }


    private fun spawnRandomTwoNails(count: Int) {
        clearBoard()

        val allowedIndexes = (0..MAX_NAIL_INDEX).toList().shuffled().take(count)
        for (index in allowedIndexes){
            val row = index / COLS
            val cols = index % COLS
            board[row][cols] = true
        }
    }

    private fun spawnNailInTopRow() {
        val emptyCols = (0 until COLS).filter {col -> !board[0][col]
        }
        if (emptyCols.isNotEmpty()) {
            val chosenCol = emptyCols.random()
            board[0][chosenCol] = true
        }
    }

    private fun handleBottomRow() {
        val bottomRow = ROWS - 1
        for (col in 0 until COLS) {
            if (board[bottomRow][col]) {
                board[bottomRow][col] = false
                spawnNailInTopRow()
            }
        }
    }

    private fun stepNailsDown() {

        for (row in ROWS - 2 downTo 0) {
            for (col in 0 until COLS) {
                if (!board[row][col])
                    continue

                val newRow = row + 1

                if (newRow == ROWS - 1 && col == currentLane) {
                    board[row][col] = false
                    onCarHit()
                    spawnNailInTopRow()
                } else {
                    board[row][col] = false
                    board[newRow][col] = true
                }
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

    private fun updateNailsUi() {
        for (nail in nails){
            nail.visibility = View.INVISIBLE
            }
        var index = 0
        for (row in 0 until ROWS) {
            for (col in 0 until COLS) {
                if (board[row][col]) {
                    nails[index].visibility = View.VISIBLE
                }
                index++
            }
        }
    }

    private fun updateHeartsUi() {
        for (i in hearts.indices) {
            hearts[i].visibility =
                if (i < lives) View.VISIBLE else View.INVISIBLE

        }
    }




}
