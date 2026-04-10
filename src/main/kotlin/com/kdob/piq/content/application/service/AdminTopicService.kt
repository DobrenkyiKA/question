package com.kdob.piq.content.application.service

import com.kdob.piq.content.domain.topic.Topic
import com.kdob.piq.content.infrastructure.persistence.topic.JpaTopicCommandRepository
import com.kdob.piq.content.infrastructure.persistence.topic.JpaTopicQueryRepository
import com.kdob.piq.content.infrastructure.web.dto.topic.CreateTopicRequest
import com.kdob.piq.content.infrastructure.web.dto.topic.UpdateTopicRequest
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class AdminTopicService(
    private val topicQueryRepository: JpaTopicQueryRepository,
    private val topicCommandRepository: JpaTopicCommandRepository
) {

    @Transactional
    fun createTopic(req: CreateTopicRequest): Topic {
        val sanitizedKey = validateAndSanitizeKey(req.key)
        val parent = req.parentPath?.let {
            topicQueryRepository.findByPath(it)
                ?: throw IllegalArgumentException("Parent topic not found: $it")
        }

        val path = parent?.path?.let { "$it/$sanitizedKey" }
            ?: "/$sanitizedKey"

        val topic = Topic(
            key = sanitizedKey,
            name = req.name,
            path = path,
            coverageArea = req.coverageArea,
            exclusions = req.exclusions
        )

        return topicCommandRepository.save(topic)
    }

    @Transactional
    fun updateTopic(key: String, req: UpdateTopicRequest): Topic {
        val sanitizedKey = validateAndSanitizeKey(req.key)
        val existing = topicQueryRepository.findByKey(key)
            ?: throw IllegalArgumentException("Topic not found: $key")

        if (existing.key == sanitizedKey && existing.name == req.name && existing.coverageArea == req.coverageArea && existing.exclusions == req.exclusions) {
            return existing
        }

        var updatedTopic = existing.copy(name = req.name, coverageArea = req.coverageArea, exclusions = req.exclusions)

        if (existing.key != sanitizedKey) {
            val oldPath = existing.path
            val parentPath = if (oldPath.contains("/")) oldPath.substringBeforeLast("/") else ""
            val newPath = if (parentPath.isEmpty()) "/$sanitizedKey" else "$parentPath/$sanitizedKey"

            updatedTopic = updatedTopic.copy(key = sanitizedKey, path = newPath)
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

    private fun validateAndSanitizeKey(key: String): String {
        val sanitized = key.lowercase().replace("\\s+".toRegex(), "-")
        if (!sanitized.matches("^[a-z0-9-]+$".toRegex())) {
            throw IllegalArgumentException("Key must contain only lowercase letters, numbers, and hyphens: $key")
        }
        return sanitized
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
        topicCommandRepository.delete(key)
    }
}