package com.kdob.piq.question.persistence.presentation

import com.kdob.piq.question.domain.presentation.Presentation
import com.kdob.piq.question.domain.presentation.PresentationCommandRepository
import org.springframework.stereotype.Repository

@Repository
class JpaPresentationCommandRepository(
    private val repo: SpringDataPresentationRepository
) : PresentationCommandRepository {

    override fun save(presentation: Presentation): Presentation =
        repo.save(presentation.toEntity()).toDomain(presentation.answers)
}