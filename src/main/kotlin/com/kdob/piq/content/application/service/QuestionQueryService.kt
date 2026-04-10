package com.kdob.piq.content.application.service

import com.kdob.piq.content.domain.question.QuestionQueryRepository
import com.kdob.piq.content.domain.question.Difficulty
import com.kdob.piq.content.domain.question.Question
import com.kdob.piq.content.domain.topic.TopicQueryRepository
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
        pageable: Pageable,
        searchTerm: String? = null,
        searchInAnswers: Boolean = false,
    ): Page<Question> {

        val expandedTopicKeys = if (topicKeys.isNotEmpty()) {
            topicKeys
                .flatMap { key ->
                    val root = topicQueryRepository.findByKey(key) ?: return@flatMap emptyList()
                    topicQueryRepository
                        .findAllByPathPrefix(root.path)
                        .map { it.key }
                }
                .toSet()
        } else {
            emptySet()
        }

        return questionRepository.findByCriteria(
            topicKeys = expandedTopicKeys,
            difficulties = difficulties,
            labels = labels,
            formats = formats,
            pageable = pageable,
            searchTerm = searchTerm,
            searchInAnswers = searchInAnswers,
        )
    }
}