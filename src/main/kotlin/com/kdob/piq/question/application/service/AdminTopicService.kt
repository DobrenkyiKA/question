package com.kdob.piq.question.application.service

import com.kdob.piq.question.domain.topic.Topic
import com.kdob.piq.question.infrastructure.persistence.topic.JpaTopicCommandRepository
import com.kdob.piq.question.infrastructure.persistence.topic.JpaTopicQueryRepository
import com.kdob.piq.question.infrastructure.web.dto.topic.CreateTopicRequest
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
            id = null,
            key = req.key,
            name = req.name,
            parentId = parent?.id,
            path = path
        )

        return topicCommandRepository.save(topic)
    }
}