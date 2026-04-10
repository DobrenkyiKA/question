package com.kdob.piq.content.infrastructure.web.dto.topic

data class CreateTopicRequest(
    val key: String,
    val name: String,
    val parentPath: String?,
    val coverageArea: String = "",
    val exclusions: String = ""
)