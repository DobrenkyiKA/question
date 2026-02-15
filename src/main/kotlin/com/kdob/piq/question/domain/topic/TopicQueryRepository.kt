package com.kdob.piq.question.domain.topic

import java.util.UUID

interface TopicQueryRepository {
    fun findAll(): List<Topic>
    fun findById(id: UUID): Topic?
    fun findByPath(path: String): Topic?
}