package com.kdob.piq.question.domain.answer

import java.util.UUID

interface AnswerCommandRepository {
    fun save(answer: Answer, presentationId: UUID): Answer
}