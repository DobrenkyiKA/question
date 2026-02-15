package com.kdob.piq.question.domain.learning

import com.kdob.piq.question.domain.presentation.Presentation
import com.kdob.piq.question.domain.presentation.PresentationFormat
import java.util.*

interface LearningQueryRepository {

    fun findPresentations(
        topicPath: String,
        format: PresentationFormat,
        state: LearningState,
        userId: UUID,
        limit: Int
    ): List<Presentation>
}