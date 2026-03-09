package com.kdob.piq.question.application.service

import com.kdob.piq.question.infrastructure.persistence.topic.SpringDataTopicRepository
import com.kdob.piq.question.infrastructure.persistence.topic.TopicEntity
import com.kdob.piq.question.infrastructure.web.dto.topic.UpdateTopicRequest
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class AdminTopicServiceUpdateTest {

    @Autowired
    private lateinit var adminTopicService: AdminTopicService

    @Autowired
    private lateinit var topicRepository: SpringDataTopicRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    @Test
    fun shouldUpdateTopicDescription() {
        // 1. Create a topic
        val topic = TopicEntity(
            key = "topic-to-update",
            name = "Original Name",
            parentId = null,
            path = "/topic-to-update",
            description = "Original Description"
        )
        entityManager.persist(topic)
        entityManager.flush()
        entityManager.clear()

        // 2. Update the topic description
        val updateRequest = UpdateTopicRequest(
            key = "topic-to-update",
            name = "Updated Name",
            description = "Updated Description"
        )
        adminTopicService.updateTopic("topic-to-update", updateRequest)
        entityManager.flush()
        entityManager.clear()

        // 3. Verify description is updated
        val updatedTopic = topicRepository.findByKey("topic-to-update")
        assertEquals("Updated Name", updatedTopic?.name)
        assertEquals("Updated Description", updatedTopic?.description)
    }

    @Test
    fun shouldUpdateTopicDescriptionToEmpty() {
        // 1. Create a topic
        val topic = TopicEntity(
            key = "topic-to-update-empty",
            name = "Original Name",
            parentId = null,
            path = "/topic-to-update-empty",
            description = "Original Description"
        )
        entityManager.persist(topic)
        entityManager.flush()
        entityManager.clear()

        // 2. Update the topic description to empty
        val updateRequest = UpdateTopicRequest(
            key = "topic-to-update-empty",
            name = "Updated Name",
            description = ""
        )
        adminTopicService.updateTopic("topic-to-update-empty", updateRequest)
        entityManager.flush()
        entityManager.clear()

        // 3. Verify description is updated to empty
        val updatedTopic = topicRepository.findByKey("topic-to-update-empty")
        assertEquals("", updatedTopic?.description)
    }
}
