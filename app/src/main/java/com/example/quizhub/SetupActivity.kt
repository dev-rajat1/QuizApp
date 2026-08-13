package com.example.quizhub

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class SetupActivity : AppCompatActivity() {

    private var selectedDifficulty = "easy"
    private var selectedAmount = 10
    private var category = 1
    private var selectedType = "multiple"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)

        //  Category receive
        category = intent.getIntExtra("category", 9)

        //  Difficulty Buttons
        val btnEasy = findViewById<MaterialButton>(R.id.btnEasy)
        val btnMedium = findViewById<MaterialButton>(R.id.btnMedium)
        val btnHard = findViewById<MaterialButton>(R.id.btnHard)

        val diffList = listOf(btnEasy, btnMedium, btnHard)

        fun selectDifficulty(selectedBtn: MaterialButton, value: String) {
            selectedDifficulty = value

            diffList.forEach {
                it.setBackgroundColor(Color.TRANSPARENT)
                it.setTextColor(Color.BLACK)
            }

            selectedBtn.setBackgroundColor(Color.parseColor("#6200EE"))
            selectedBtn.setTextColor(Color.WHITE)
        }

        btnEasy.setOnClickListener { selectDifficulty(btnEasy, "easy") }
        btnMedium.setOnClickListener { selectDifficulty(btnMedium, "medium") }
        btnHard.setOnClickListener { selectDifficulty(btnHard, "hard") }

        //  Question Buttons
        val btn1 = findViewById<MaterialButton>(R.id.btn1)
        val btn2 = findViewById<MaterialButton>(R.id.btn2)
        val btn3 = findViewById<MaterialButton>(R.id.btn3)
        val btn4 = findViewById<MaterialButton>(R.id.btn4)


        val countList = listOf(btn1, btn2, btn3, btn4)

        fun selectAmount(selectedBtn: MaterialButton, value: Int) {
            selectedAmount = value

            countList.forEach {
                it.setBackgroundColor(Color.TRANSPARENT)
                it.setTextColor(Color.BLACK)
            }

            selectedBtn.setBackgroundColor(Color.parseColor("#6200EE"))
            selectedBtn.setTextColor(Color.WHITE)
        }

        btn1.setOnClickListener { selectAmount(btn1, 10) }
        btn2.setOnClickListener { selectAmount(btn2, 20) }
        btn3.setOnClickListener { selectAmount(btn3, 30) }
        btn4.setOnClickListener { selectAmount(btn4, 40) }

        //    Type Buttons

        val btnMultiple = findViewById<MaterialButton>(R.id.btnMultiple)
        val btnBoolean = findViewById<MaterialButton>(R.id.btnBoolean)

        val typeList = listOf(btnMultiple, btnBoolean)

        fun selectType (selectedBtn: MaterialButton, value: String) {
            selectedType = value

            typeList.forEach {
                it.setBackgroundColor(Color.TRANSPARENT)
                it.setTextColor(Color.BLACK)
            }
            selectedBtn.setBackgroundColor(Color.parseColor("#6200EE"))
            selectedBtn.setTextColor(Color.WHITE)
        }
        btnMultiple.setOnClickListener  { selectType( btnMultiple,"multiple") }
        btnBoolean.setOnClickListener  { selectType( btnBoolean,"boolean") }

//  Default selection (optional )

        selectDifficulty(btnEasy, "easy")
        selectAmount(btn1, 10)
        selectType(btnMultiple,"multiple")

        //  Start Button

        val btnStart = findViewById<MaterialButton>(R.id.btnStart)


        btnStart.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("category", category)
            intent.putExtra("difficulty", selectedDifficulty)
            intent.putExtra("amount", selectedAmount)
            intent.putExtra("type", selectedType)
            startActivity(intent)
        }
    }
}