package com.kdob.piq.question.infrastructure.web.dto.topic

data class CreateTopicRequest(
    val key: String,
    val name: String,
    val parentPath: String?
)