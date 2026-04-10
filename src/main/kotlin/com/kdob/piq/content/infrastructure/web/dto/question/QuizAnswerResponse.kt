package com.kdob.piq.content.infrastructure.web.dto.question

/**
 * Single quiz answer option.
 */
data class QuizAnswerResponse(
    val text: String,
    val correct: Boolean,
    val explanation: String?
)