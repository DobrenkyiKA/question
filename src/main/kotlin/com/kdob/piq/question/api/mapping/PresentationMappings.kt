package com.kdob.piq.question.api.mapping

import com.kdob.piq.question.api.dto.AnswerResponse
import com.kdob.piq.question.api.dto.PresentationResponse
import com.kdob.piq.question.domain.presentation.Presentation

fun Presentation.toResponse(): PresentationResponse =
    PresentationResponse(
        itemId = learningItemId,
        format = format,
        prompt = prompt,
        answers = answers.map {
            AnswerResponse(
                text = it.text,
                correct = it.correct,
                explanation = it.explanation
            )
        }
    )