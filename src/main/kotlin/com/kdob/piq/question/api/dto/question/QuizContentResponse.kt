package com.kdob.piq.question.api.dto.question

/**
 * Quiz content for a question.
 */
data class QuizContentResponse(
    val answers: List<QuizAnswerResponse>
)