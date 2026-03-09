package com.kdob.piq.question.infrastructure.persistence.topic

import com.kdob.piq.question.domain.topic.Topic
import com.kdob.piq.question.domain.topic.TopicCommandRepository
import org.springframework.stereotype.Repository

@Repository
class JpaTopicCommandRepository(private val repo: SpringDataTopicRepository) : TopicCommandRepository {
    override fun save(topic: Topic): Topic {
        val existing = repo.findByKey(topic.key)
        val parentPath = if (topic.path.contains("/")) topic.path.substringBeforeLast("/") else null
        val parentId = parentPath?.let {
            if (it.isEmpty()) null else repo.findByPath(it)?.id
        }

        val entity = existing?.apply {
            name = topic.name
            path = topic.path
            this.parentId = parentId
        } ?: topic.toEntity(parentId)

        return repo.saveAndFlush(entity).toDomain()
    }

    override fun update(oldKey: String, topic: Topic): Topic {
        val entity = repo.findByKey(oldKey)
            ?: throw NoSuchElementException("Topic not found by key: $oldKey")

        val parentPath = if (topic.path.contains("/")) topic.path.substringBeforeLast("/") else null
        val parentId = parentPath?.let {
            if (it.isEmpty()) null else repo.findByPath(it)?.id
        }

        entity.key = topic.key
        entity.name = topic.name
        entity.path = topic.path
        entity.parentId = parentId

        return repo.saveAndFlush(entity).toDomain()
    }

    override fun delete(key: String) {
        repo.deleteByKey(key)
    }
}