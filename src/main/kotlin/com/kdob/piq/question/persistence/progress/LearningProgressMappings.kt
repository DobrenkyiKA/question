package com.kdob.piq.question.persistence.progress

import com.kdob.piq.question.domain.progress.LearningProgress

fun LearningProgressEntity.toDomain(): LearningProgress =
    LearningProgress(
        userId = userId,
        learningItemId = learningItemId,
        learnedAt = learnedAt
    )