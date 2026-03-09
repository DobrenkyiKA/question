package com.kdob.piq.question.infrastructure.web.mapping

import com.kdob.piq.question.domain.topic.Topic
import com.kdob.piq.question.infrastructure.web.dto.topic.TopicResponse

fun Topic.toResponse(): TopicResponse =
    TopicResponse(
        key = key,
        name = name,
        path = path,
        coverageArea = coverageArea,
        exclusions = exclusions
    )

