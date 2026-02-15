package com.kdob.piq.question.api.dto

import com.kdob.piq.question.domain.presentation.PresentationFormat
import java.util.UUID

data class PresentationResponse(
    val itemId: UUID,
    val format: PresentationFormat,
    val prompt: String,
    val answers: List<AnswerResponse>
)