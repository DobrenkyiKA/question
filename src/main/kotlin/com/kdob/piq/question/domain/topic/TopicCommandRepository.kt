package com.kdob.piq.question.domain.topic

interface TopicCommandRepository {
    fun save(topic: Topic): Topic
}