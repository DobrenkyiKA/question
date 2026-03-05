package com.kdob.piq.question.infrastructure.web.dto.topic

data class TopicResponse(
    val id: Long,
    val key: String,
    val name: String,
    val parentId: Long?,
    val path: String
)