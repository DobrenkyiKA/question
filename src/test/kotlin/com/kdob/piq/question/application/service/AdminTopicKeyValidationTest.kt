package com.kdob.piq.question.application.service

import com.kdob.piq.question.infrastructure.web.dto.topic.CreateTopicRequest
import com.kdob.piq.question.infrastructure.web.dto.topic.UpdateTopicRequest
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AdminTopicKeyValidationTest {

    @Autowired
    private lateinit var adminTopicService: AdminTopicService

    @Test
    fun `should reject key with special characters`() {
        val request = CreateTopicRequest(
            key = "invalid@key",
            name = "Invalid Topic",
            parentPath = null
        )
        assertThrows(IllegalArgumentException::class.java) {
            adminTopicService.createTopic(request)
        }
    }

    @Test
    fun `should sanitize key with spaces and uppercase`() {
        val request = CreateTopicRequest(
            key = "Spring Boot",
            name = "Spring Boot",
            parentPath = null
        )
        val created = adminTopicService.createTopic(request)
        assert(created.key == "spring-boot")
    }

    @Test
    fun `should reject update with invalid key`() {
        val createRequest = CreateTopicRequest(
            key = "valid-key",
            name = "Valid",
            parentPath = null
        )
        val created = adminTopicService.createTopic(createRequest)

        val updateRequest = UpdateTopicRequest(
            key = "invalid key!",
            name = "Invalid"
        )
        assertThrows(IllegalArgumentException::class.java) {
            adminTopicService.updateTopic(created.key, updateRequest)
        }
    }
}
