package com.kdob.piq.question.domain.answer

import java.util.UUID

data class Answer(
    val id: UUID?,
    val text: String,
    val correct: Boolean,
    val explanation: String?
)