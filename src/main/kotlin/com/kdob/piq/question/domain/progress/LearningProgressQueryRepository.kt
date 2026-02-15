package com.kdob.piq.question.domain.progress

import java.util.UUID

interface LearningProgressQueryRepository {
    fun isLearned(userId: UUID, itemId: UUID): Boolean
}