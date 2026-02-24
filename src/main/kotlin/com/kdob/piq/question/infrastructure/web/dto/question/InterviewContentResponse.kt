package com.kdob.piq.question.infrastructure.web.dto.question

/**
 * Interview content for a question.
 * Exists only if the question supports interview format.
 */
data class InterviewContentResponse(
    val shortAnswer: String,
    val longAnswer: String?
)