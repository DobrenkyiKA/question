package com.kdob.piq.question.application.service

import com.kdob.piq.question.domain.question.QuestionQueryRepository
import com.kdob.piq.question.domain.question.Difficulty
import com.kdob.piq.question.domain.question.Question
import com.kdob.piq.question.domain.topic.TopicQueryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class QuestionQueryService(
    private val questionRepository: QuestionQueryRepository,
    private val topicQueryRepository: TopicQueryRepository
) {

    @Transactional(readOnly = true)
    fun findQuestions(
        topicKeys: Set<String>,
        difficulties: Set<Difficulty>,
        labels: Set<String>,
        formats: Set<String>,
        pageable: Pageable
    ): Page<Question> {

        val topicIds = if (topicKeys.isNotEmpty()) {
            topicKeys
                .flatMap { key ->
                    val root = topicQueryRepository.findByKey(key)
                    topicQueryRepository
                        .findAllByPathPrefix(root.path)
                        .mapNotNull { it.id }
                }
                .toSet()
        } else {
            emptySet()
        }

        return questionRepository.findByCriteria(
            topicIds = topicIds,
            difficulties = difficulties,
            labels = labels,
            formats = formats,
            pageable = pageable
        )
    }
}