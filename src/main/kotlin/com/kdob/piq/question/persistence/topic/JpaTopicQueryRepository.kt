package com.kdob.piq.question.persistence.topic

import com.kdob.piq.question.domain.topic.Topic
import com.kdob.piq.question.domain.topic.TopicQueryRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class JpaTopicQueryRepository(
    private val repo: SpringDataTopicRepository
) : TopicQueryRepository {

    override fun findAll(): List<Topic> =
        repo.findAll().map { it.toDomain() }

    override fun findById(id: UUID): Topic? =
        repo.findById(id).map { it.toDomain() }.orElse(null)

    override fun findByPath(path: String): Topic? =
        repo.findByPath(path)?.toDomain()
}