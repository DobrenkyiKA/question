package com.kdob.piq.question.infrastructure.persistence.topic

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SpringDataTopicRepository : JpaRepository<TopicEntity, Long> {
    fun findByPath(path: String): TopicEntity?
    fun findByKey(key: String): TopicEntity?
    fun findByPathStartingWith(path: String): List<TopicEntity>
}