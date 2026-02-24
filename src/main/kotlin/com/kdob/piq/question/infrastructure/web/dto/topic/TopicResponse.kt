package com.kdob.piq.question.infrastructure.web.dto.topic

import java.util.UUID

data class TopicResponse(
    val id: UUID,
    val key: String,
    val name: String,
    val parentId: UUID?,
    val path: String
)