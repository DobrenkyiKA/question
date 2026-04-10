package com.kdob.piq.content.domain.question

data class Question(
    val key: String,
    val prompt: String,
    val difficulty: Difficulty,
    val topicKey: String,
    val labels: Set<String> = emptySet(),
    val interviewContent: InterviewContent? = null,
    val quizContent: QuizContent? = null
)

data class InterviewContent(
    val shortAnswer: String,
    val longAnswer: String? = null
)

data class QuizContent(
    val answers: List<QuizAnswer> = emptyList()
)

data class QuizAnswer(
    val text: String,
    val correct: Boolean,
    val explanation: String? = null
)