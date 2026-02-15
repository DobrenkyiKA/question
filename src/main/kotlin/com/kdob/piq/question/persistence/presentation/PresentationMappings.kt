package com.kdob.piq.question.persistence.presentation

import com.kdob.piq.question.domain.presentation.Presentation
import com.kdob.piq.question.domain.answer.Answer

fun PresentationEntity.toDomain(answers: List<Answer>): Presentation =
    Presentation(
        id = id,
        learningItemId = learningItemId,
        format = format,
        prompt = prompt,
        answers = answers
    )

fun Presentation.toEntity(): PresentationEntity =
    PresentationEntity(
        id = id,
        learningItemId = learningItemId,
        format = format,
        prompt = prompt
    )