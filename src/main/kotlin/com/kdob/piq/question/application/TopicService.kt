package com.kdob.piq.question.application

import com.kdob.piq.question.domain.topic.Topic
import com.kdob.piq.question.persistence.topic.JpaTopicQueryRepository
import org.springframework.stereotype.Service

@Service
class TopicService(
    private val topicRepository: JpaTopicQueryRepository
) {

    fun getAllTopics(): List<Topic> =
        topicRepository.findAll()
}