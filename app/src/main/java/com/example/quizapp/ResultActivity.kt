package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quizapp.databinding.ActivityResultBinding
import com.google.firebase.auth.FirebaseAuth

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔹 Get data from Intent
        val score = intent.getIntExtra("score", 0)
        val total = intent.getIntExtra("total", 0)

        binding.tvScore.text = "You scored $score / $total"

        // 🔹 Restart Quiz button
        binding.btnRestart.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 🔹 Logout button
        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()  // Logout from Firebase
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}