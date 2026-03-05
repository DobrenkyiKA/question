package com.kdob.piq.question.domain.question

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface QuestionQueryRepository {
    fun findByCriteria(
        topicKeys: Set<String>,
        difficulties: Set<Difficulty>,
        labels: Set<String>,
        formats: Set<String>,
        pageable: Pageable,
    ): Page<Question>
}
