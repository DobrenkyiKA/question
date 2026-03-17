package com.kdob.piq.question.application.service

import com.kdob.piq.question.domain.question.Difficulty
import com.kdob.piq.question.infrastructure.persistence.question.InterviewContentEntity
import com.kdob.piq.question.infrastructure.persistence.question.QuestionEntity
import com.kdob.piq.question.infrastructure.persistence.question.QuestionRepository
import com.kdob.piq.question.infrastructure.persistence.topic.SpringDataTopicRepository
import com.kdob.piq.question.infrastructure.persistence.topic.TopicEntity
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class AdminTopicServiceDeleteTest {

    @Autowired
    private lateinit var adminTopicService: AdminTopicService

    @Autowired
    private lateinit var topicRepository: SpringDataTopicRepository

    @Autowired
    private lateinit var questionRepository: QuestionRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    @Test
    fun shouldDeleteTopicWithQuestionsAndAllContent() {
        // 1. Create a topic
        val topic = TopicEntity(
            key = "topic-to-delete",
            name = "Topic to Delete",
            parentId = null,
            path = "/topic-to-delete"
        )
        entityManager.persist(topic)

        // 2. Create a question for that topic
        val question = QuestionEntity(
            key = "question-to-delete",
            prompt = "What is test?",
            difficulty = Difficulty.MEDIUM,
            topic = topic
        )
        question.labels = setOf("test-label")
        entityManager.persist(question)

        // 3. Create interview content for that question
        val interviewContent = InterviewContentEntity(
            question = question,
            shortAnswer = "short answer",
            longAnswer = "long answer"
        )
        question.interviewContent = interviewContent
        entityManager.persist(interviewContent)

        entityManager.flush()
        entityManager.clear() // Clear to ensure we test re-fetching

        // 4. Attempt to delete the topic
        adminTopicService.deleteTopic("topic-to-delete")
        entityManager.flush()
        entityManager.clear()

        // 5. Verify topic is gone
        assertNull(topicRepository.findByKey("topic-to-delete"))
        // 6. Verify question is gone
        assertNull(questionRepository.findByKey("question-to-delete"))

        // 7. Verify interview content is gone (using native query for direct DB check)
        val interviewContentCount = entityManager.createNativeQuery(
            "SELECT count(*) FROM interview_content WHERE short_answer = 'short answer'"
        ).singleResult as Number
        assertEquals(0, interviewContentCount.toInt(), "Interview content should be deleted")

        // 8. Verify labels are gone
        val labelsCount = entityManager.createNativeQuery(
            "SELECT count(*) FROM question_labels WHERE label = 'test-label'"
        ).singleResult as Number
        assertEquals(0, labelsCount.toInt(), "Question labels should be deleted")
    }

    @Test
    fun shouldDeleteParentTopicAndChildShouldBeDeletedByCascade() {
        // 1. Create a parent topic
        val parent = TopicEntity(
            key = "parent-cascade",
            name = "Parent Cascade",
            parentId = null,
            path = "/parent-cascade"
        )
        entityManager.persist(parent)
        entityManager.flush()

        // 2. Create a child topic pointing to parent
        val child = TopicEntity(
            key = "child-cascade",
            name = "Child Cascade",
            parentId = parent.id,
            path = "/parent-cascade/child-cascade"
        )
        entityManager.persist(child)
        entityManager.flush()
        entityManager.clear()

        // 3. Attempt to delete parent via service (this should now SUCCEED)
        adminTopicService.deleteTopic("parent-cascade")
        entityManager.flush()
        entityManager.clear()

        // 4. Verify parent is gone
        assertNull(topicRepository.findByKey("parent-cascade"))
        // 5. Verify child is gone (via database cascade)
        assertNull(topicRepository.findByKey("child-cascade"), "Child topic should be deleted by database cascade")
    }
}
