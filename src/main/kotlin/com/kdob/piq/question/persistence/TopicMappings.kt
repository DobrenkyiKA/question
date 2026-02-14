package com.kdob.piq.question.persistence

import com.kdob.piq.question.domain.Topic

fun TopicEntity.toDomain() =
    Topic(
        id = id,
        key = key,
        name = name,
        parentId = parentId,
        path = path
    )