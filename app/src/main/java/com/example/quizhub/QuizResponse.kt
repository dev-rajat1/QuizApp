package com.example.quizhub

data class QuizResponse(
    val response_code: Int,
    val results: List<Question>
)

data class Question(
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>
)
