package com.kdob.piq.question.persistence.answer

import com.kdob.piq.question.domain.answer.Answer
import com.kdob.piq.question.domain.answer.AnswerCommandRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class JpaAnswerCommandRepository(
    private val repo: SpringDataAnswerRepository
) : AnswerCommandRepository {

    override fun save(
        answer: Answer,
        presentationId: UUID
    ): Answer =
        repo.save(answer.toEntity(presentationId)).toDomain()
}
