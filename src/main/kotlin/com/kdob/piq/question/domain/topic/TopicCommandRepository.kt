package com.kdob.piq.question.domain.topic

interface TopicCommandRepository {
    fun save(topic: Topic): Topic
    fun update(oldKey: String, topic: Topic): Topic
    fun delete(key: String)
}