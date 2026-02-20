package com.kdob.piq.question.application.question

import com.kdob.piq.question.domain.quesiton.Difficulty
import com.kdob.piq.question.persistence.QuestionEntity
import com.kdob.piq.question.persistence.QuestionRepository
import com.kdob.piq.question.persistence.QuestionSpecifications
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service

@Service
class QuestionQueryService(
    private val repository: QuestionRepository
) {

    fun findQuestions(
        topicKeys: Set<String>,
        difficulties: Set<Difficulty>,
        labels: Set<String>,
        formats: Set<String>,
        pageable: Pageable
    ): Page<QuestionEntity> {

        var spec: Specification<QuestionEntity> = Specification { _, _, _ -> null }

        if (topicKeys.isNotEmpty()) {
            spec = spec.and(QuestionSpecifications.hasAnyTopic(topicKeys))
        }

        if (difficulties.isNotEmpty()) {
            spec = spec.and(QuestionSpecifications.hasDifficulty(difficulties))
        }

        if (labels.isNotEmpty()) {
            spec = spec.and(QuestionSpecifications.hasAnyLabel(labels))
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

        return repository.findAll(spec, pageable)
    }
}