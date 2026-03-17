package com.kdob.piq.question.infrastructure.web.dto.topic

data class TopicResponse(
    val key: String,
    val name: String,
    val path: String,
    val coverageArea: String = "",
    val exclusions: String = "",
    val questionCount: Int = 0,
    val childCount: Int = 0
)