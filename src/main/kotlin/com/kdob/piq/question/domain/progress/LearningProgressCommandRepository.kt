package com.kdob.piq.question.domain.progress

import java.time.Instant
import java.util.UUID

interface LearningProgressCommandRepository {

    fun markLearned(
        userId: UUID,
        learningItemId: UUID,
        at: Instant
    )
}