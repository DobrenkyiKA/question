package com.kdob.piq.question.infrastructure.web.dto.question

/**
 * Quiz content for a question.
 */
data class QuizContentResponse(
    val answers: List<QuizAnswerResponse>
)