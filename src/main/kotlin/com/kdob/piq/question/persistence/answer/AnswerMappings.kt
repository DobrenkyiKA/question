package com.kdob.piq.question.persistence.answer

import com.kdob.piq.question.domain.answer.Answer
import java.util.UUID


fun AnswerEntity.toDomain(): Answer =
    Answer(
        id = id,
        text = text,
        correct = correct,
        explanation = explanation
    )

fun Answer.toEntity(presentationId: UUID): AnswerEntity =
    AnswerEntity(
        id = id,
        presentationId = presentationId,
        text = text,
        correct = correct,
        explanation = explanation
    )