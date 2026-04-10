package com.kdob.piq.content.infrastructure.web

import com.kdob.piq.content.infrastructure.web.dto.topic.CreateTopicRequest
import com.kdob.piq.content.infrastructure.web.dto.topic.UpdateTopicRequest
import com.kdob.piq.content.infrastructure.web.dto.topic.TopicResponse
import com.kdob.piq.content.infrastructure.web.mapping.toResponse
import com.kdob.piq.content.application.service.AdminTopicService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/topics")
class AdminTopicController(
    private val topicService: AdminTopicService
) {

    @PostMapping
    fun create(@RequestBody req: CreateTopicRequest): TopicResponse =
        topicService.createTopic(req).toResponse()

    @PutMapping("/{key}")
    fun update(@PathVariable key: String, @RequestBody req: UpdateTopicRequest): TopicResponse =
        topicService.updateTopic(key, req).toResponse()

    @PatchMapping("/{key}/move")
    fun move(@PathVariable key: String, @RequestParam(required = false) parentPath: String?): TopicResponse =
        topicService.moveTopic(key, parentPath).toResponse()

    @DeleteMapping("/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable key: String) {
        topicService.deleteTopic(key)
    }
}