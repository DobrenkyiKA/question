package com.kdob.piq.question.infrastructure.persistence.question

import com.kdob.piq.question.domain.question.Difficulty
import com.kdob.piq.question.domain.question.Question
import com.kdob.piq.question.domain.question.QuestionQueryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class JpaQuestionQueryRepository(
    private val repository: QuestionRepository
) : QuestionQueryRepository {

    override fun findByCriteria(
        topicIds: Set<UUID>,
        difficulties: Set<Difficulty>,
        labels: Set<String>,
        formats: Set<String>,
        pageable: Pageable
    ): Page<Question> {
        var spec: Specification<QuestionEntity> = Specification { _, _, _ -> null }

        if (topicIds.isNotEmpty()) {
            spec = spec.and(QuestionSpecifications.hasAnyTopicId(topicIds))
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
