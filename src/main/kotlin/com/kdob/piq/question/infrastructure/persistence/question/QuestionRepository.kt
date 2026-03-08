package com.kdob.piq.question.infrastructure.persistence.question

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.UUID

interface QuestionRepository :
    JpaRepository<QuestionEntity, Long>,
    JpaSpecificationExecutor<QuestionEntity> {
    fun findByKey(key: String): QuestionEntity?
}