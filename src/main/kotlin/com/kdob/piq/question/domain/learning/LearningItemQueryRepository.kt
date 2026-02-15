package com.kdob.piq.question.domain.learning

import java.util.UUID

interface LearningItemQueryRepository {
    fun findById(id: UUID): LearningItem?
}