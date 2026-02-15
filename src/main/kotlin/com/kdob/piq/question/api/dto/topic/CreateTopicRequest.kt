package com.kdob.piq.question.api.dto.topic

data class CreateTopicRequest(
    val key: String,
    val name: String,
    val parentPath: String?
)