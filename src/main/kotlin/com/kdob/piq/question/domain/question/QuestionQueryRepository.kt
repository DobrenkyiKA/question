package com.kdob.piq.question.domain.question

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface QuestionQueryRepository {
    fun findByCriteria(
        topicIds: Set<UUID>,
        difficulties: Set<Difficulty>,
        labels: Set<String>,
        formats: Set<String>,
        pageable: Pageable
    ): Page<Question>
}
