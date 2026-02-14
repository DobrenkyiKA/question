package com.kdob.piq.question.application

import com.kdob.piq.question.domain.Topic
import com.kdob.piq.question.persistence.JpaTopicRepository
import org.springframework.stereotype.Service

@Service
class TopicService(
    private val topicRepository: JpaTopicRepository
) {

    fun getAllTopics(): List<Topic> =
        topicRepository.findAll()
}