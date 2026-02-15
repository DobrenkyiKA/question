package com.kdob.piq.question.application

import com.kdob.piq.question.domain.learning.LearningQueryRepository
import com.kdob.piq.question.domain.learning.LearningState
import com.kdob.piq.question.domain.presentation.PresentationFormat
import com.kdob.piq.question.domain.progress.LearningProgressCommandRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class LearningService(
    private val queryRepo: LearningQueryRepository,
    private val progressCommandRepo: LearningProgressCommandRepository
) {

    fun getPresentations(
        topicPath: String,
        format: PresentationFormat,
        state: LearningState,
        userId: UUID,
        limit: Int
    ) =
        queryRepo.findPresentations(topicPath, format, state, userId, limit)

    fun markLearned(userId: UUID, itemId: UUID) {
        progressCommandRepo.markLearned(userId, itemId, Instant.now())
    }
}