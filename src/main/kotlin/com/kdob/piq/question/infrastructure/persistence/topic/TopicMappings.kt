package com.kdob.piq.question.infrastructure.persistence.topic.topic

import com.kdob.piq.question.domain.topic.Topic

fun TopicEntity.toDomain(): Topic =
    Topic(
        id = id,
        key = key,
        name = name,
        parentId = parentId,
        path = path
    )

fun Topic.toEntity(): TopicEntity =
    TopicEntity(
        id = id,
        key = key,
        name = name,
        parentId = parentId,
        path = path
    )