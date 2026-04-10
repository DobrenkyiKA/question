package com.kdob.piq.content.infrastructure.persistence.topic

import com.kdob.piq.content.domain.topic.Topic

fun TopicEntity.toDomain(): Topic =
    Topic(
        key = key,
        name = name,
        path = path,
        coverageArea = coverageArea,
        exclusions = exclusions,
        questionCount = questions.size,
        childCount = children.size
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