package com.kdob.piq.question.domain.topic

import java.util.UUID

data class Topic(
    val id: UUID?,
    val key: String,
    val name: String,
    val parentId: UUID?,
    val path: String
)