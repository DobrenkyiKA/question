package com.kdob.piq.content.infrastructure.web.dto.question

/**
 * Interview content for a question.
 * Exists only if the question supports interview format.
 */
data class InterviewContentResponse(
    val shortAnswer: String,
    val longAnswer: String?
)