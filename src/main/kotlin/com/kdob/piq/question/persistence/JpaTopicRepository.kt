package com.kdob.piq.question.persistence

import com.kdob.piq.question.domain.Topic
import com.kdob.piq.question.domain.TopicRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class JpaTopicRepository(
    private val repo: SpringDataTopicRepository
) : TopicRepository {

    override fun findAll(): List<Topic> =
        repo.findAll().map { it.toDomain() }

    override fun findById(id: UUID): Topic? =
        repo.findById(id).map { it.toDomain() }.orElse(null)
}