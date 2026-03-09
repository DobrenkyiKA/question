package com.kdob.piq.question.infrastructure.web

import com.kdob.piq.question.infrastructure.web.dto.topic.TopicResponse
import com.kdob.piq.question.application.service.TopicService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import com.kdob.piq.question.infrastructure.web.mapping.toResponse

@RestController
@RequestMapping("/topics")
class TopicController(
    private val topicService: TopicService
) {

    @GetMapping
    fun getTopics(): List<TopicResponse> =
        topicService.getAllTopics().map { it.toResponse() }
}