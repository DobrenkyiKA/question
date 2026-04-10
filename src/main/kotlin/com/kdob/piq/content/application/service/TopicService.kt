package com.kdob.piq.content.application.service

import com.kdob.piq.content.domain.topic.Topic
import com.kdob.piq.content.domain.topic.TopicQueryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class TopicService(
    private val topicRepository: TopicQueryRepository
) {

    fun getAllTopics(): List<Topic> =
        topicRepository.findAll()

    fun getTopicByKey(key: String): Topic? =
        topicRepository.findByKey(key)
}