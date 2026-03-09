package com.kdob.piq.question.domain.topic

interface TopicQueryRepository {
    fun findByKey(key: String): Topic?
    fun findAllByPathPrefix(path: String): List<Topic>
    fun findAll(): List<Topic>
    fun hasChildren(path: String): Boolean
}