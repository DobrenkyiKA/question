package com.kdob.piq.question.application.question

import com.kdob.piq.question.api.dto.question.QuestionResponse
import com.kdob.piq.question.api.mapping.QuestionMapper
import com.kdob.piq.question.domain.quesiton.Difficulty
import com.kdob.piq.question.domain.topic.TopicQueryRepository
import com.kdob.piq.question.persistence.QuestionEntity
import com.kdob.piq.question.persistence.QuestionRepository
import com.kdob.piq.question.persistence.QuestionSpecifications
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class QuestionQueryService(
    private val questionRepository: QuestionRepository,
    private val topicQueryRepository: TopicQueryRepository
) {

    @Transactional(readOnly = true)
    fun findQuestions(
        topicKeys: Set<String>,
        difficulties: Set<Difficulty>,
        labels: Set<String>,
        formats: Set<String>,
        pageable: Pageable
    ): Page<QuestionResponse> {

        var spec: Specification<QuestionEntity> = Specification { _, _, _ -> null }

        if (topicKeys.isNotEmpty()) {
            val topicIds = topicKeys
                .flatMap { key ->
                    val root = topicQueryRepository.findByKey(key)
                    topicQueryRepository
                        .findAllByPathPrefix(root.path)
                        .mapNotNull { it.id }
                }
                .toSet()

            spec = spec.and(
                QuestionSpecifications.hasAnyTopicId(topicIds)
            )
        }

        if (difficulties.isNotEmpty()) {
            spec = spec.and(
                QuestionSpecifications.hasDifficulty(difficulties)
            )
        }

        if (labels.isNotEmpty()) {
            spec = spec.and(
                QuestionSpecifications.hasAnyLabel(labels)
            )
        }

        if (formats.isNotEmpty()) {
            var formatSpec: Specification<QuestionEntity>? = null

            if ("INTERVIEW" in formats) {
                formatSpec = QuestionSpecifications.supportsInterview()
            }
            if ("QUIZ" in formats) {
                formatSpec = formatSpec?.or(QuestionSpecifications.supportsQuiz())
                    ?: QuestionSpecifications.supportsQuiz()
            }

            formatSpec?.let {
                spec = spec.and(it)
            }
        }

        return questionRepository.findAll(spec, pageable).map { QuestionMapper.toResponse(it) }
    }
}