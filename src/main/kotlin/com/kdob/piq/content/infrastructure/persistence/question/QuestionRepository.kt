package com.kdob.piq.content.infrastructure.persistence.question

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface QuestionRepository :
    JpaRepository<QuestionEntity, Long>,
    JpaSpecificationExecutor<QuestionEntity> {
    fun findByKey(key: String): QuestionEntity?
}