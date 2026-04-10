package com.kdob.piq.content.application.service

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class QuestionPromptQueryServiceTest {

    @Autowired
    private lateinit var queryService: QuestionPromptQueryService

    @Test
    fun `findPromptsByTopicKeys should not throw LazyInitializationException`() {
        // Given
        // We assume 'java' is a topic key from DataInitializer
        val topicKeys = setOf("java")

        // When
        // Calling it from outside a transaction, which is what happens in the controller
        val results = queryService.findPromptsByTopicKeys(topicKeys)

        // Then
        // Should not throw LazyInitializationException when accessing topicKey
        println("[DEBUG_LOG] Found ${results.size} prompts for topics: $topicKeys")
        results.forEach {
            println("[DEBUG_LOG] Prompt: ${it.prompt}, TopicKey: ${it.topicKey}")
        }
    }
}
