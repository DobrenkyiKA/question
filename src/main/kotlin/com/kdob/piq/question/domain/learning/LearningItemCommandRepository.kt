package com.kdob.piq.question.domain.learning

import java.util.UUID

interface LearningItemCommandRepository {
    fun save(item: LearningItem): LearningItem
    fun attachToTopics(itemId: UUID, topicIds: List<UUID>)
}