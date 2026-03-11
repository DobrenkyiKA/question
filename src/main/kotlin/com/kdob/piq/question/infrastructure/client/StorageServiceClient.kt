package com.kdob.piq.question.infrastructure.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class StorageServiceClient(
    private val restTemplate: RestTemplate,
    @Value("\${storage.url}") private val baseUrl: String
) {

    fun getVersions(): List<String> {
        val typeRef = object : ParameterizedTypeReference<List<String>>() {}
        return restTemplate.exchange("$baseUrl/versions/questions", HttpMethod.GET, null, typeRef).body ?: emptyList()
    }

    fun refresh() {
        restTemplate.postForLocation("$baseUrl/versions/refresh", null)
    }

    fun deleteVersion(version: String) {
        restTemplate.delete("$baseUrl/versions/questions/$version")
    }

    fun getLastCommitMessage(version: String): String {
        return restTemplate.getForObject("$baseUrl/versions/questions/$version/last-commit-message", String::class.java) ?: ""
    }

    fun listTopics(version: String): List<String> {
        val typeRef = object : ParameterizedTypeReference<List<String>>() {}
        return restTemplate.exchange("$baseUrl/versions/questions/$version/topics", HttpMethod.GET, null, typeRef).body ?: emptyList()
    }

    fun getTopicFile(version: String, fileName: String): String? {
        return restTemplate.getForObject("$baseUrl/versions/questions/$version/topics/$fileName", String::class.java)
    }

    fun saveTopicFile(version: String, fileName: String, content: String) {
        restTemplate.put("$baseUrl/versions/questions/$version/topics/$fileName", content)
    }

    fun listQuestions(version: String): List<String> {
        val typeRef = object : ParameterizedTypeReference<List<String>>() {}
        return restTemplate.exchange("$baseUrl/versions/questions/$version/questions", HttpMethod.GET, null, typeRef).body ?: emptyList()
    }

    fun getQuestionFile(version: String, fileName: String): String? {
        return restTemplate.getForObject("$baseUrl/versions/questions/$version/questions/$fileName", String::class.java)
    }

    fun saveQuestionFile(version: String, fileName: String, content: String) {
        restTemplate.put("$baseUrl/versions/questions/$version/questions/$fileName", content)
    }

    fun commit(version: String, message: String) {
        restTemplate.postForLocation("$baseUrl/versions/questions/$version/commit", message)
    }
}
