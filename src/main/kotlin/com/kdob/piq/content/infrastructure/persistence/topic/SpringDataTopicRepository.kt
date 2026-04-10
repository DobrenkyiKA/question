package com.kdob.piq.content.infrastructure.persistence.topic

import org.springframework.data.jpa.repository.JpaRepository

interface SpringDataTopicRepository : JpaRepository<TopicEntity, Long> {
    fun findByPath(path: String): TopicEntity?
    fun findByKey(key: String): TopicEntity?
    fun findByPathStartingWith(path: String): List<TopicEntity>
    fun findAllByOrderByNameAsc(): List<TopicEntity>
    fun findByParentIdIsNull(): List<TopicEntity>
    fun existsByPathStartingWith(path: String): Boolean
    fun deleteByKey(key: String)
}