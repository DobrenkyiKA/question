package com.kdob.piq.content.domain.topic

interface TopicCommandRepository {
    fun save(topic: Topic): Topic
    fun update(oldKey: String, topic: Topic): Topic
    fun delete(key: String)
}