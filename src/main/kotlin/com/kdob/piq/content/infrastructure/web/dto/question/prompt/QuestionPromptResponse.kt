package com.kdob.piq.content.infrastructure.web.dto.question.prompt

/**
 * Lightweight DTO representing an existing
 * question prompt for AI deduplication.
 */
data class QuestionPromptResponse (
    val prompt: String,
    val topicKey: String
)