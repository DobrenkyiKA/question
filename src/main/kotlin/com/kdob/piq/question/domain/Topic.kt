package com.kdob.piq.question.domain

import java.util.UUID

data class Topic(
    val id: UUID?,
    val name: String,
    val parentId: UUID?
)