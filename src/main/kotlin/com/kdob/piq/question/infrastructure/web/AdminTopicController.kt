package com.kdob.piq.question.infrastructure.web

import com.kdob.piq.question.infrastructure.web.dto.topic.CreateTopicRequest
import com.kdob.piq.question.infrastructure.web.dto.topic.TopicResponse
import com.kdob.piq.question.infrastructure.web.mapping.toResponse
import com.kdob.piq.question.application.service.AdminTopicService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/topics")
class AdminTopicController(
    private val topicService: AdminTopicService
) {

    @PostMapping
    fun create(@RequestBody req: CreateTopicRequest): TopicResponse =
        topicService.createTopic(req).toResponse()
}