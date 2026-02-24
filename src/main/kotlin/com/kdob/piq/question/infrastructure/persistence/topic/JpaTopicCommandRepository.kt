package com.kdob.piq.question.infrastructure.persistence.topic.topic

import com.kdob.piq.question.domain.topic.Topic
import com.kdob.piq.question.domain.topic.TopicCommandRepository
import org.springframework.stereotype.Repository

@Repository
class JpaTopicCommandRepository (private val repo: SpringDataTopicRepository) : TopicCommandRepository {
    override fun save(topic: Topic): Topic =
        repo.saveAndFlush(topic.toEntity()).toDomain()
}