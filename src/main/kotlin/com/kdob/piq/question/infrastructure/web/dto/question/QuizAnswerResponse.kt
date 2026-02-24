package com.kdob.piq.question.infrastructure.web.dto.question

/**
 * Single quiz answer option.
 */
data class QuizAnswerResponse(
    val text: String,
    val correct: Boolean,
    val explanation: String?
)