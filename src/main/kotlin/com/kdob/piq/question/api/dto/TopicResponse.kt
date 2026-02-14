package com.kdob.piq.question.api.dto

import java.util.UUID

data class TopicResponse(
    val id: UUID,
    val name: String,
    val parentId: UUID?
)