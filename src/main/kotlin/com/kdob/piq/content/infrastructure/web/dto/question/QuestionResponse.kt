package com.kdob.piq.content.infrastructure.web.dto.question

import com.kdob.piq.content.domain.question.Difficulty

/**
 * QuestionResponse is the canonical API representation
 * of a Question for both admin and user UIs.
 */
data class QuestionResponse(
    val key: String,
    val prompt: String,
    val difficulty: Difficulty,
    val labels: Set<String>,
    val topic: String,
    val interview: InterviewContentResponse?,
    val quiz: QuizContentResponse?
)