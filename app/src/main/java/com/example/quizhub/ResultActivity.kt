package com.example.quizhub

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        //  Bind Views
        val tvScore = findViewById<TextView>(R.id.tvScore)
        val tvMessage = findViewById<TextView>(R.id.tvMessage)

        val btnRetry = findViewById<MaterialButton>(R.id.btnRetry)
        val btnHome = findViewById<MaterialButton>(R.id.btnHome)

        //  Data Receive

        val score = intent.getIntExtra("score", 0)
        val total = intent.getIntExtra("total", 10)

        //  Score Show
        tvScore.text = "$score / $total"

        //  Percentage Calculate
        val percentage = (score * 100) / total

        //  Message Logic
        val message = when {
            percentage >= 80 -> "Excellent 🔥"
            percentage >= 60 -> "Very Good 💯"
            percentage >= 40 -> "Good 👍"
            else -> "Try Again 😅"
        }

        tvMessage.text = message

        //  Retry Button
        btnRetry.setOnClickListener {
            val intent = Intent(this, SetupActivity::class.java)
            startActivity(intent)
            finish()
        }

        //  Home Button
        btnHome.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}