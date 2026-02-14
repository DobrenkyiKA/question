package com.kdob.piq.question.persistence

import com.kdob.piq.question.domain.Topic

fun TopicEntity.toDomain(): Topic =
    Topic(
        id = id,
        name = name,
        parentId = parentId
    )