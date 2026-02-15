package com.kdob.piq.question.domain.progress

import java.time.Instant
import java.util.UUID

data class LearningProgress(
    val userId: UUID,
    val learningItemId: UUID,
    val learnedAt: Instant?
)