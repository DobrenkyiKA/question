package com.kdob.piq.content.infrastructure.web.mapping

import com.kdob.piq.content.domain.topic.Topic
import com.kdob.piq.content.infrastructure.web.dto.topic.TopicResponse

fun Topic.toResponse(): TopicResponse =
    TopicResponse(
        key = key,
        name = name,
        path = path,
        coverageArea = coverageArea,
        exclusions = exclusions,
        questionCount = questionCount,
        childCount = childCount
    )

