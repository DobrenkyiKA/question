package com.kdob.piq.question.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SpringDataTopicRepository : JpaRepository<TopicEntity, UUID>