package com.kdob.piq.question

import com.kdob.piq.question.application.question.QuestionQueryService
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest

@SpringBootTest
class QuestionApplicationTests {

    @Autowired
    private lateinit var questionQueryService: QuestionQueryService

    @Test
    fun contextLoads() {
    }

    @Test
    fun labelsShouldBeInitializedAfterServiceReturns() {
        val page = questionQueryService.findQuestions(
            topicKeys = emptySet(),
            difficulties = emptySet(),
            labels = emptySet(),
            formats = emptySet(),
            pageable = PageRequest.of(0, 10)
        )

        assertFalse(page.isEmpty, "Should have seeded data")
        val question = page.content[0]
        assertNotNull(question.labels)

        // This should trigger LazyInitializationException if it's not initialized
        println("[DEBUG_LOG] Labels: ${question.labels}")
        assertFalse(question.labels.isEmpty(), "Should have at least one label")
    }
}
