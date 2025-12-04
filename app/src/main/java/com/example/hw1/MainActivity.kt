package com.example.hw1

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var gameManager: GameManager

    private lateinit var btnLeft: ImageButton
    private lateinit var btnRight: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initGame()
        initListeners()
    }

    private fun initViews() {
        val carLeft: ImageView  = findViewById(R.id.main_IMG_car0)
        val carMid: ImageView   = findViewById(R.id.main_IMG_car1)
        val carRight: ImageView = findViewById(R.id.main_IMG_car2)
        val laneCars = arrayOf(carLeft, carMid, carRight)

        val nails = arrayOf(
            findViewById<ImageView>(R.id.main_IMG_nail1),
            findViewById(R.id.main_IMG_nail2),
            findViewById(R.id.main_IMG_nail3),
            findViewById(R.id.main_IMG_nail4),
            findViewById(R.id.main_IMG_nail5),
            findViewById(R.id.main_IMG_nail6),
            findViewById(R.id.main_IMG_nail7),
            findViewById(R.id.main_IMG_nail8),
            findViewById(R.id.main_IMG_nail9),
            findViewById(R.id.main_IMG_nail10),
            findViewById(R.id.main_IMG_nail11),
            findViewById(R.id.main_IMG_nail12),
            findViewById(R.id.main_IMG_nail13),
            findViewById(R.id.main_IMG_nail14),
            findViewById(R.id.main_IMG_nail15),
            findViewById(R.id.main_IMG_nail16),
            findViewById(R.id.main_IMG_nail17),
            findViewById(R.id.main_IMG_nail18)
        )

        val hearts = arrayOf(
            findViewById<ImageView>(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )

        gameManager = GameManager(laneCars, nails, hearts)

        gameManager.onCarHitListener = {
            runOnUiThread {
                Toast.makeText(this, "Crash!", Toast.LENGTH_SHORT).show()
            }
        }


        btnLeft = findViewById(R.id.btn_left)
        btnRight = findViewById(R.id.btn_right)
    }

    private fun initGame() {
        gameManager.startGame()
    }

    private fun initListeners() {
        btnLeft.setOnClickListener {
            gameManager.moveCarLeft()
        }

        btnRight.setOnClickListener {
            gameManager.moveCarRight()
        }
    }

}

