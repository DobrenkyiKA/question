package com.kdob.piq.question.api.dto.question

import com.kdob.piq.question.domain.quesiton.Difficulty
import java.util.*

/**
 * QuestionResponse is the canonical API representation
 * of a Question for both admin and user UIs.
 */
data class QuestionResponse(
    val id: UUID,
    val key: String,
    val prompt: String,
    val difficulty: Difficulty,
    val labels: Set<String>,
    val topic: String,
    val interview: InterviewContentResponse?,
    val quiz: QuizContentResponse?
)