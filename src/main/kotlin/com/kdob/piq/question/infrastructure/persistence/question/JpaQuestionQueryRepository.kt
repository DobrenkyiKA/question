package com.kdob.piq.question.infrastructure.persistence.question

import com.kdob.piq.question.domain.question.Difficulty
import com.kdob.piq.question.domain.question.Question
import com.kdob.piq.question.domain.question.QuestionQueryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Repository

@Repository
class JpaQuestionQueryRepository(
    private val repository: QuestionRepository
) : QuestionQueryRepository {

    override fun findByCriteria(
        topicKeys: Set<String>,
        difficulties: Set<Difficulty>,
        labels: Set<String>,
        formats: Set<String>,
        pageable: Pageable
    ): Page<Question> {
        var spec: Specification<QuestionEntity> = Specification { _, _, _ -> null }

        if (topicKeys.isNotEmpty()) {
            spec = spec.and(QuestionSpecifications.hasAnyTopicKey(topicKeys))
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

        return repository.findAll(spec, pageable).map { it.toDomain() }
    }
}
