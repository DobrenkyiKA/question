package com.kdob.piq.question.api.dto.topic

import java.util.UUID

data class TopicResponse(
    val id: UUID,
    val key: String,
    val name: String,
    val parentId: UUID?,
    val path: String
)