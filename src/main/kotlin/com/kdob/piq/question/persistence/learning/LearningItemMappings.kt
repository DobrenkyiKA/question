package com.kdob.piq.question.persistence.learning

import com.kdob.piq.question.domain.learning.LearningItem

fun LearningItemEntity.toDomain(): LearningItem =
    LearningItem(id = id)

fun LearningItem.toEntity(): LearningItemEntity =
    LearningItemEntity(id = id)