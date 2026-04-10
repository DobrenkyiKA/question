package com.kdob.piq.content.application.service

import com.kdob.piq.content.infrastructure.persistence.topic.SpringDataTopicRepository
import com.kdob.piq.content.infrastructure.persistence.topic.TopicEntity
import com.kdob.piq.content.infrastructure.web.dto.topic.UpdateTopicRequest
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
    fun shouldUpdateTopicCoverageArea() {
        // 1. Create a topic
        val topic = TopicEntity(
            key = "topic-to-update",
            name = "Original Name",
            parentId = null,
            path = "/topic-to-update",
            coverageArea = "Original Coverage Area",
            exclusions = "Original Exclusions"
        )
        entityManager.persist(topic)
        entityManager.flush()
        entityManager.clear()

        // 2. Update the topic coverageArea
        val updateRequest = UpdateTopicRequest(
            key = "topic-to-update",
            name = "Updated Name",
            coverageArea = "Updated Coverage Area",
            exclusions = "Updated Exclusions"
        )
        adminTopicService.updateTopic("topic-to-update", updateRequest)
        entityManager.flush()
        entityManager.clear()

        // 3. Verify properties are updated
        val updatedTopic = topicRepository.findByKey("topic-to-update")
        assertEquals("Updated Name", updatedTopic?.name)
        assertEquals("Updated Coverage Area", updatedTopic?.coverageArea)
        assertEquals("Updated Exclusions", updatedTopic?.exclusions)
    }

    @Test
    fun shouldUpdateTopicCoverageAreaToEmpty() {
        // 1. Create a topic
        val topic = TopicEntity(
            key = "topic-to-update-empty",
            name = "Original Name",
            parentId = null,
            path = "/topic-to-update-empty",
            coverageArea = "Original Coverage Area",
            exclusions = "Original Exclusions"
        )
        entityManager.persist(topic)
        entityManager.flush()
        entityManager.clear()

        // 2. Update the topic coverageArea to empty
        val updateRequest = UpdateTopicRequest(
            key = "topic-to-update-empty",
            name = "Updated Name",
            coverageArea = "",
            exclusions = ""
        )
        adminTopicService.updateTopic("topic-to-update-empty", updateRequest)
        entityManager.flush()
        entityManager.clear()

        // 3. Verify coverageArea is updated to empty
        val updatedTopic = topicRepository.findByKey("topic-to-update-empty")
        assertEquals("", updatedTopic?.coverageArea)
        assertEquals("", updatedTopic?.exclusions)
    }
}
