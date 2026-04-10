package com.kdob.piq.content.infrastructure.web

import com.kdob.piq.content.infrastructure.web.dto.topic.TopicResponse
import com.kdob.piq.content.application.service.TopicService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

import com.kdob.piq.content.infrastructure.web.mapping.toResponse

@RestController
@RequestMapping("/topics")
class TopicController(
    private val topicService: TopicService
) {

    @GetMapping
    fun getTopics(): List<TopicResponse> =
        topicService.getAllTopics().map { it.toResponse() }

    @GetMapping("/{key}")
    fun getTopic(@PathVariable key: String): TopicResponse =
        topicService.getTopicByKey(key)?.toResponse()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found: $key")
}