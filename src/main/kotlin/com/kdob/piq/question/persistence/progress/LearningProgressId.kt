package com.kdob.piq.question.persistence.progress

import java.io.Serializable
import java.util.UUID

data class LearningProgressId(
    val userId: UUID,
    val learningItemId: UUID
) : Serializable