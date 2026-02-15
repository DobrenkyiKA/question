package com.kdob.piq.question.api.dto

data class AnswerResponse(
    val text: String,
    val correct: Boolean,
    val explanation: String?
)