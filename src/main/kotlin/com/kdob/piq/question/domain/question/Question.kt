package com.kdob.piq.question.domain.question

import java.util.UUID

data class Question(
    val id: Long? = null,
    val key: String,
    val prompt: String,
    val difficulty: Difficulty,
    val topicId: Long,
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