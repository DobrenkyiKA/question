package com.kdob.piq.question.domain.topic

data class Topic(
    val id: Long?,
    val key: String,
    val name: String,
    val parentId: Long?,
    val path: String
)