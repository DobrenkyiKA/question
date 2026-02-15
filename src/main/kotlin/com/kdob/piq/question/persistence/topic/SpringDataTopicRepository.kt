package com.kdob.piq.question.persistence.topic

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SpringDataTopicRepository : JpaRepository<TopicEntity, UUID> {
    fun findByPath(path: String): TopicEntity?
}