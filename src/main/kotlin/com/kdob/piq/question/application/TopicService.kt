package com.kdob.piq.question.application

import com.kdob.piq.question.domain.Topic
import com.kdob.piq.question.domain.TopicRepository
import org.springframework.stereotype.Service

@Service
class TopicService(
    private val topicRepository: TopicRepository
) {
    fun getAllTopics(): List<Topic> =
        topicRepository.findAll()
}