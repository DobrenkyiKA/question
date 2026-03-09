package com.kdob.piq.question.application.service

import com.kdob.piq.question.domain.topic.Topic
import com.kdob.piq.question.infrastructure.persistence.topic.JpaTopicCommandRepository
import com.kdob.piq.question.infrastructure.persistence.topic.JpaTopicQueryRepository
import com.kdob.piq.question.infrastructure.web.dto.topic.CreateTopicRequest
import com.kdob.piq.question.infrastructure.web.dto.topic.UpdateTopicRequest
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class AdminTopicService(
    private val topicQueryRepository: JpaTopicQueryRepository,
    private val topicCommandRepository: JpaTopicCommandRepository
) {

    @Transactional
    fun createTopic(req: CreateTopicRequest): Topic {
        val parent = req.parentPath?.let {
            topicQueryRepository.findByPath(it)
                ?: throw IllegalArgumentException("Parent topic not found: $it")
        }

        val path = parent?.path?.let { "$it/${req.key}" }
            ?: "/${req.key}"

        val topic = Topic(
            key = req.key,
            name = req.name,
            path = path
        )

        return topicCommandRepository.save(topic)
    }

    @Transactional
    fun updateTopic(key: String, req: UpdateTopicRequest): Topic {
        val existing = topicQueryRepository.findByKey(key)
            ?: throw IllegalArgumentException("Topic not found: $key")

        if (existing.key == req.key && existing.name == req.name) {
            return existing
        }

        var updatedTopic = existing.copy(name = req.name)

        if (existing.key != req.key) {
            val oldPath = existing.path
            val parentPath = if (oldPath.contains("/")) oldPath.substringBeforeLast("/") else ""
            val newPath = if (parentPath.isEmpty()) "/${req.key}" else "$parentPath/${req.key}"

            updatedTopic = updatedTopic.copy(key = req.key, path = newPath)
            val result = topicCommandRepository.update(key, updatedTopic)

            // Update children paths
            val descendants = topicQueryRepository.findAllByPathPrefix("$oldPath/")
            descendants.forEach { descendant ->
                val updatedDescendant = descendant.copy(
                    path = descendant.path.replaceFirst(oldPath, newPath)
                )
                topicCommandRepository.save(updatedDescendant)
            }
            return result
        }

        return topicCommandRepository.update(key, updatedTopic)
    }

    @Transactional
    fun moveTopic(key: String, newParentPath: String?): Topic {
        val topic = topicQueryRepository.findByKey(key)
            ?: throw IllegalArgumentException("Topic not found: $key")

        val newParent = newParentPath?.let {
            topicQueryRepository.findByPath(it)
                ?: throw IllegalArgumentException("New parent topic not found: $it")
        }

        val oldPath = topic.path
        val newPath = newParent?.let { "${it.path}/${topic.key}" } ?: "/${topic.key}"

        if (oldPath == newPath) {
            return topic
        }

        // Prevent moving a topic into its own descendant
        if (newPath.startsWith("$oldPath/")) {
            throw IllegalArgumentException("Cannot move a topic into its own descendant")
        }

        val updatedTopic = topic.copy(path = newPath)
        val result = topicCommandRepository.update(key, updatedTopic)

        // Update descendants
        val descendants = topicQueryRepository.findAllByPathPrefix("$oldPath/")
        descendants.forEach { descendant ->
            val updatedDescendant = descendant.copy(
                path = descendant.path.replaceFirst(oldPath, newPath)
            )
            topicCommandRepository.save(updatedDescendant)
        }

        return result
    }

    @Transactional
    fun deleteTopic(key: String) {
        val topic = topicQueryRepository.findByKey(key)
            ?: throw IllegalArgumentException("Topic not found: $key")

        if (topicQueryRepository.hasChildren(topic.path)) {
            throw IllegalStateException("Cannot delete topic with child topics")
        }
        topicCommandRepository.delete(key)
    }
}