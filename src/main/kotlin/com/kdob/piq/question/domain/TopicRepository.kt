package com.kdob.piq.question.domain

import java.util.UUID

interface TopicRepository {
    fun findAll(): List<Topic>
    fun findById(id: UUID): Topic?
}