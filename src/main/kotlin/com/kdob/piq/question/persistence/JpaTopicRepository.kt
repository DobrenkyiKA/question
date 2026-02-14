package com.kdob.piq.question.persistence

import com.kdob.piq.question.domain.Topic
import com.kdob.piq.question.domain.TopicRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class JpaTopicRepository(
    private val repo: SpringDataTopicRepository
) {

    fun findAll(): List<Topic> =
        repo.findAll().map { it.toDomain() }

    fun findByPath(path: String): Topic? =
        repo.findByPath(path)?.toDomain()
}