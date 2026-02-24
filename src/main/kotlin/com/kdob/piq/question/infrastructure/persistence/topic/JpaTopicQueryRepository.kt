package com.kdob.piq.question.infrastructure.persistence.topic.topic

import com.kdob.piq.question.domain.topic.Topic
import com.kdob.piq.question.domain.topic.TopicQueryRepository
import org.springframework.stereotype.Repository

@Repository
class JpaTopicQueryRepository(
    private val repo: SpringDataTopicRepository
) : TopicQueryRepository {

    override fun findByKey(key: String): Topic =
        repo.findByKey(key)?.toDomain() ?: throw NoSuchElementException("Topic not found by key: $key")

    override fun findAllByPathPrefix(path: String): List<Topic> =
        repo.findByPathStartingWith(path).map { it.toDomain() }

    override fun findAll(): List<Topic> =
        repo.findAll().map { it.toDomain() }

    fun findByPath(path: String): Topic? =
        repo.findByPath(path)?.toDomain()
}