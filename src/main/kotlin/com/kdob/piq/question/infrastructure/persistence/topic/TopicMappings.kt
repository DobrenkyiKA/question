package com.kdob.piq.question.infrastructure.persistence.topic

import com.kdob.piq.question.domain.topic.Topic

fun TopicEntity.toDomain(): Topic =
    Topic(
        key = key,
        name = name,
        path = path,
        coverageArea = coverageArea,
        exclusions = exclusions
    )

fun Topic.toEntity(parentId: Long? = null): TopicEntity =
    TopicEntity(
        key = key,
        name = name,
        parentId = parentId,
        path = path,
        coverageArea = coverageArea,
        exclusions = exclusions
    )