package com.example.quizhub

import android.content.Intent
import android.graphics.Color
import android.os.*
import android.text.Html
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var tvQuestion: TextView
    private lateinit var tvProgress: TextView
    private lateinit var loaderLayout: View
    private lateinit var btn1: MaterialButton
    private lateinit var btn2: MaterialButton
    private lateinit var btn3: MaterialButton
    private lateinit var btn4: MaterialButton

    private lateinit var questionList: List<Question>

    private lateinit var tvTimer: TextView

    private var timer: CountDownTimer? = null

    private var currentIndex = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //  Bind views
        loaderLayout = findViewById(R.id.loaderLayout)
        tvQuestion = findViewById(R.id.tvQuestion)
        tvProgress = findViewById(R.id.tvProgress)
        tvTimer = findViewById(R.id.tvTimer)

        btn1 = findViewById(R.id.btn1)
        btn2 = findViewById(R.id.btn2)
        btn3 = findViewById(R.id.btn3)
        btn4 = findViewById(R.id.btn4)

        // 🎯 Receive data
        val category = intent.getIntExtra("category", 18)
        val difficulty = intent.getStringExtra("difficulty") ?: "easy"
        val amount = intent.getIntExtra("amount", 10)
        val type = intent.getStringExtra("type") ?: "multiple"
        fetchQuestions(amount, category, difficulty,type)
    }

    //  API CALL
    private fun fetchQuestions(amount: Int, category: Int, difficulty: String, type: String) {

        loaderLayout.visibility = View.VISIBLE

        RetrofitClient.api.getQuestions(amount, category, difficulty, type)
            .enqueue(object : Callback<QuizResponse> {

                override fun onResponse(call: Call<QuizResponse>, response: Response<QuizResponse>) {

                    loaderLayout.visibility = View.GONE

                    if (response.isSuccessful && response.body() != null) {
                        questionList = response.body()!!.results
                        showQuestion()
                    } else {
                        Toast.makeText(this@MainActivity, "No data found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<QuizResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    //  SHOW QUESTION
    private fun showQuestion() {

        resetButtons()
        startTimer()

        val q = questionList[currentIndex]
        val correctAnswer = q.correct_answer

        //  Progress
        tvProgress.text = "Question ${currentIndex + 1}/${questionList.size}"

        //  Question (HTML safe)
        tvQuestion.text = Html.fromHtml(q.question, Html.FROM_HTML_MODE_LEGACY)

        //  Type check (IMPORTANT)
        val type = intent.getStringExtra("type") ?: "multiple"

        //  OPTIONS BUILD
        val options = mutableListOf<String>()
        options.add(correctAnswer)
        options.addAll(q.incorrect_answers)
        options.shuffle()


        //  BOOLEAN HANDLING FIX

        if (type == "boolean") {

            // show only 2 buttons
            btn1.visibility = View.VISIBLE
            btn2.visibility = View.VISIBLE
            btn3.visibility = View.GONE
            btn4.visibility = View.GONE

            btn1.text = Html.fromHtml(options.getOrNull(0) ?: "True", Html.FROM_HTML_MODE_LEGACY)
            btn2.text = Html.fromHtml(options.getOrNull(1) ?: "False", Html.FROM_HTML_MODE_LEGACY)

            btn1.setOnClickListener { checkAnswer(btn1, correctAnswer) }
            btn2.setOnClickListener { checkAnswer(btn2, correctAnswer) }

        } else {

            // show all buttons
            btn1.visibility = View.VISIBLE
            btn2.visibility = View.VISIBLE
            btn3.visibility = View.VISIBLE
            btn4.visibility = View.VISIBLE

            btn1.text = Html.fromHtml(options.getOrNull(0) ?: "", Html.FROM_HTML_MODE_LEGACY)
            btn2.text = Html.fromHtml(options.getOrNull(1) ?: "", Html.FROM_HTML_MODE_LEGACY)
            btn3.text = Html.fromHtml(options.getOrNull(2) ?: "", Html.FROM_HTML_MODE_LEGACY)
            btn4.text = Html.fromHtml(options.getOrNull(3) ?: "", Html.FROM_HTML_MODE_LEGACY)

            btn1.setOnClickListener { checkAnswer(btn1, correctAnswer) }
            btn2.setOnClickListener { checkAnswer(btn2, correctAnswer) }
            btn3.setOnClickListener { checkAnswer(btn3, correctAnswer) }
            btn4.setOnClickListener { checkAnswer(btn4, correctAnswer) }
        }
    }
    private fun startTimer() {

        timer?.cancel()

        var time = 10
        tvTimer.text = time.toString()

        timer = object : CountDownTimer(10000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                time--
                tvTimer.text = time.toString()

                //  last 3 sec warning

                if (time <= 3) {
                    tvTimer.setTextColor(Color.RED)
                } else {
                    tvTimer.setTextColor(Color.WHITE)
                }
            }

            override fun onFinish() {
                moveToNextQuestion() // auto skip
            }
        }.start()
    }
    private fun moveToNextQuestion() {

        // stop timer first
        timer?.cancel()

        // reset timer UI
        tvTimer.setTextColor(Color.WHITE)

        currentIndex++

        if (currentIndex < questionList.size) {

            // next question show
            showQuestion()

        } else {

            //  quiz finished  go result screen

            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("score", score)
            intent.putExtra("total", questionList.size)
            startActivity(intent)

            finish()
        }
    }

    //  CHECK ANSWER
    private fun checkAnswer(selectedBtn: MaterialButton, correctAnswer: String) {

        disableButtons()
        timer?.cancel()
        tvTimer.setTextColor(Color.WHITE)

        val selectedText = selectedBtn.text.toString()

        if (selectedText == correctAnswer) {
            selectedBtn.setBackgroundColor(Color.parseColor("#4CAF50")) // green
            score++
        } else {
            selectedBtn.setBackgroundColor(Color.parseColor("#F44336")) // red

            //  show correct answer also

            showCorrectAnswer(correctAnswer)
        }

        //  delay → next question
        Handler(Looper.getMainLooper()).postDelayed({

            currentIndex++

            if (currentIndex < questionList.size) {
                showQuestion()
            } else {
                // 👉 go to result screen
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("score", score)
                intent.putExtra("total", questionList.size)
                startActivity(intent)
                finish()
            }

        }, 1000)
    }

    // SHOW CORRECT OPTION
    private fun showCorrectAnswer(correctAnswer: String) {

        val buttons = listOf(btn1, btn2, btn3, btn4)

        for (btn in buttons) {
            if (btn.text.toString() == correctAnswer) {
                btn.setBackgroundColor(Color.parseColor("#4CAF50"))
            }
        }
    }

    // RESET BUTTONS
    private fun resetButtons() {

        val buttons = listOf(btn1, btn2, btn3, btn4)

        buttons.forEach {
            it.setBackgroundColor(Color.TRANSPARENT)
            it.setTextColor(Color.BLACK)
            it.isEnabled = true
            it.visibility = View.VISIBLE
        }
    }

    // DISABLE AFTER CLICK
    private fun disableButtons() {
        btn1.isEnabled = false
        btn2.isEnabled = false
        btn3.isEnabled = false
        btn4.isEnabled = false
    }
}