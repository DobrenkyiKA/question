package com.kdob.piq.question.domain.topic

data class Topic(
    val key: String,
    val name: String,
    val path: String,
    val coverageArea: String = "",
    val exclusions: String = ""
)