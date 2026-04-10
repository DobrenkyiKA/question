package com.kdob.piq.content.infrastructure.web.dto.topic

data class UpdateTopicRequest(
    val key: String,
    val name: String,
    val coverageArea: String = "",
    val exclusions: String = ""
)
