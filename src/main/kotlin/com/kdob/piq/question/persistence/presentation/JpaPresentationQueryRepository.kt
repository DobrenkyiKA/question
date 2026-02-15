package com.kdob.piq.question.persistence.presentation

import com.kdob.piq.question.domain.presentation.Presentation
import com.kdob.piq.question.domain.presentation.PresentationQueryRepository
import com.kdob.piq.question.persistence.answer.SpringDataAnswerRepository
import com.kdob.piq.question.persistence.answer.toDomain
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class JpaPresentationQueryRepository(
    private val presentationRepo: SpringDataPresentationRepository,
    private val answerRepo: SpringDataAnswerRepository
) : PresentationQueryRepository {

    override fun findById(id: UUID): Presentation? {
        val presentationEntity = presentationRepo.findById(id).orElse(null)
            ?: return null

        val answers = answerRepo
            .findAllByPresentationId(id)
            .map { it.toDomain() }

        return presentationEntity.toDomain(answers)
    }
}