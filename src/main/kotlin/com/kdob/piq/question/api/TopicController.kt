package com.kdob.piq.question.api

import com.kdob.piq.question.api.dto.TopicResponse
import com.kdob.piq.question.application.TopicService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/topics")
class TopicController(
    private val topicService: TopicService
) {

    @GetMapping
    fun getTopics(): List<TopicResponse> =
        topicService.getAllTopics().map {
            TopicResponse(it.id!!, it.name, it.parentId)
        }
}