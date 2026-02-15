package com.kdob.piq.question.domain.presentation

import com.kdob.piq.question.domain.answer.Answer
import java.util.*

data class Presentation(
    val id: UUID?,
    val learningItemId: UUID,
    val format: PresentationFormat,
    val prompt: String,
    val answers: List<Answer>
)