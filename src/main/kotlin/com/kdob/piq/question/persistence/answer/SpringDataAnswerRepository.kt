package com.kdob.piq.question.persistence.answer

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SpringDataAnswerRepository :
    JpaRepository<AnswerEntity, UUID> {

    fun findAllByPresentationId(presentationId: UUID): List<AnswerEntity>
}