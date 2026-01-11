package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizapp.databinding.ActivityQuizBinding
import com.example.quizapp.model.Question
import com.google.firebase.firestore.FirebaseFirestore

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding

    private val db = FirebaseFirestore.getInstance()
    private val questionList = mutableListOf<Question>()

    private var currentIndex = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchQuestions()

        // 🔹 Next button click → handleNextButton()
        binding.btnNext.setOnClickListener {
            handleNextButton()
        }
    }

    // 🔹 Firestore se questions load
    private fun fetchQuestions() {
        db.collection("questions")
            .get()
            .addOnSuccessListener { result ->
                questionList.clear()
                for (doc in result) {
                    val question = doc.toObject(Question::class.java)
                    questionList.add(question)
                }

                if (questionList.isNotEmpty()) {
                    currentIndex = 0
                    showQuestion()
                } else {
                    Toast.makeText(this, "No questions found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load questions", Toast.LENGTH_SHORT).show()
            }
    }

    // 🔹 Show question on screen
    private fun showQuestion() {
        val q = questionList[currentIndex]
        binding.tvQuestion.text = q.question
        binding.rb1.text = q.option1
        binding.rb2.text = q.option2
        binding.rb3.text = q.option3
        binding.rb4.text = q.option4

        binding.radioGroup.clearCheck()
    }

    // 🔹 Handle Next Button
    private fun handleNextButton() {
        val selectedId = binding.radioGroup.checkedRadioButtonId
        if (selectedId == -1) {
            Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedAnswer = when (selectedId) {
            binding.rb1.id -> binding.rb1.text.toString()
            binding.rb2.id -> binding.rb2.text.toString()
            binding.rb3.id -> binding.rb3.text.toString()
            binding.rb4.id -> binding.rb4.text.toString()
            else -> ""
        }

        val correctAnswer = questionList[currentIndex].correctAnswer

        if (selectedAnswer.trim().lowercase().equals(correctAnswer.trim().lowercase(), ignoreCase = true)){
            score++
        }

        // 🔹 Last question check
        if (currentIndex == questionList.size - 1) {
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("score", score)
            intent.putExtra("total", questionList.size)
            startActivity(intent)
            finish()
        } else {
            currentIndex++
            showQuestion()
        }
    }
}