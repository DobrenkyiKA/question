package com.kdob.piq.content.application.service

import com.kdob.piq.content.infrastructure.persistence.topic.SpringDataTopicRepository
import com.kdob.piq.content.infrastructure.persistence.topic.TopicEntity
import com.kdob.piq.content.infrastructure.web.dto.topic.UpdateTopicRequest
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class AdminTopicServiceMoveTest {

    @Autowired
    private lateinit var adminTopicService: AdminTopicService

    @Autowired
    private lateinit var topicRepository: SpringDataTopicRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    @Test
    fun shouldMoveTopicAndChildren() {
        // Setup hierarchy
        // /parent1
        // /parent2
        // /parent1/child1
        // /parent1/child1/grandchild1

        val p1 = createTopic("parent1", "/parent1")
        val p2 = createTopic("parent2", "/parent2")
        val c1 = createTopic("child1", "/parent1/child1", p1.id)
        val gc1 = createTopic("grandchild1", "/parent1/child1/grandchild1", c1.id)

        entityManager.flush()
        entityManager.clear()

        // Move c1 to p2
        adminTopicService.moveTopic("child1", "/parent2")

        entityManager.flush()
        entityManager.clear()

        // Verify c1 moved
        val movedC1 = topicRepository.findByKey("child1")!!
        assertEquals("/parent2/child1", movedC1.path)
        assertEquals(p2.id, movedC1.parentId)

        // Verify grandchild moved too
        val movedGc1 = topicRepository.findByKey("grandchild1")!!
        assertEquals("/parent2/child1/grandchild1", movedGc1.path)
        assertEquals(movedC1.id, movedGc1.parentId)
    }

    @Test
    fun shouldMoveTopicToRoot() {
        val p1 = createTopic("parent1", "/parent1")
        val c1 = createTopic("child1", "/parent1/child1", p1.id)

        entityManager.flush()
        entityManager.clear()

        // Move c1 to root
        adminTopicService.moveTopic("child1", null)

        entityManager.flush()
        entityManager.clear()

        val movedC1 = topicRepository.findByKey("child1")!!
        assertEquals("/child1", movedC1.path)
        assertEquals(null, movedC1.parentId)
    }

    @Test
    fun shouldThrowWhenMovingIntoDescendant() {
        val p1 = createTopic("parent1", "/parent1")
        val c1 = createTopic("child1", "/parent1/child1", p1.id)

        entityManager.flush()
        entityManager.clear()

        // Try to move p1 to c1
        assertThrows(IllegalArgumentException::class.java) {
            adminTopicService.moveTopic("parent1", "/parent1/child1")
        }
    }

    @Test
    fun shouldUpdateChildrenPathsWhenRenamingTopic() {
        val p1 = createTopic("parent1", "/parent1")
        val c1 = createTopic("child1", "/parent1/child1", p1.id)

        entityManager.flush()
        entityManager.clear()

        // Rename parent1 to parent1-new
        adminTopicService.updateTopic("parent1", UpdateTopicRequest("parent1-new", "Parent 1 New"))

        entityManager.flush()
        entityManager.clear()

        val movedP1 = topicRepository.findByKey("parent1-new")!!
        assertEquals("/parent1-new", movedP1.path)

        val movedC1 = topicRepository.findByKey("child1")!!
        assertEquals("/parent1-new/child1", movedC1.path)
        assertEquals(movedP1.id, movedC1.parentId)
    }

    private fun createTopic(key: String, path: String, parentId: Long? = null): TopicEntity {
        val topic = TopicEntity(
            key = key,
            name = key.replaceFirstChar { it.uppercase() },
            parentId = parentId,
            path = path
        )
        entityManager.persist(topic)
        return topic
    }
}
