package com.kdob.piq.question.infrastructure.web.mapping

import com.kdob.piq.question.infrastructure.web.dto.topic.TopicResponse
import com.kdob.piq.question.domain.topic.Topic

fun Topic.toResponse(): TopicResponse =
    TopicResponse(
        id = id!!,
        key = key,
        name = name,
        parentId = parentId,
        path = path
    )

