package com.kdob.piq.question.application

import com.kdob.piq.question.api.dto.CreateLearningItemRequest
import com.kdob.piq.question.domain.learning.LearningItem
import com.kdob.piq.question.domain.learning.LearningItemCommandRepository
import com.kdob.piq.question.persistence.topic.JpaTopicQueryRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class AdminLearningItemService(
    private val itemRepo: LearningItemCommandRepository,
    private val topicRepo: JpaTopicQueryRepository
) {

    @Transactional
    fun createItem(req: CreateLearningItemRequest): LearningItem {
        val item = itemRepo.save(LearningItem(id = null))

        val topics = req.topicPaths.map {
            topicRepo.findByPath(it) ?: throw IllegalArgumentException("Topic not found: $it")
        }

        itemRepo.attachToTopics(item.id!!, topics.map { it.id!! })
        return item
    }
}