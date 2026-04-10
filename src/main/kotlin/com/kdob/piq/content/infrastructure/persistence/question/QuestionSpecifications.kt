package com.kdob.piq.content.infrastructure.persistence.question

import com.kdob.piq.content.domain.question.Difficulty
import com.kdob.piq.content.infrastructure.persistence.topic.TopicEntity
import jakarta.persistence.criteria.JoinType
import org.springframework.data.jpa.domain.Specification

object QuestionSpecifications {

    fun hasDifficulty(difficulties: Set<Difficulty>): Specification<QuestionEntity> =
        Specification { root, _, _ ->
            root.get<Difficulty>("difficulty").`in`(difficulties)
        }

    fun hasAnyLabel(labels: Set<String>): Specification<QuestionEntity> =
        Specification { root, _, _ ->
            val labelsJoin = root.join<QuestionEntity, String>("labels", JoinType.INNER)
            labelsJoin.`in`(labels)
        }

    fun supportsInterview(): Specification<QuestionEntity> =
        Specification { root, _, cb ->
            cb.isNotNull(root.get<Any>("interviewContent"))
        }

    fun supportsQuiz(): Specification<QuestionEntity> =
        Specification { root, _, cb ->
            cb.isNotNull(root.get<Any>("quizContent"))
        }

    fun hasAnyTopicKey(topicKeys: Set<String>): Specification<QuestionEntity> =
        Specification { root, _, _ ->
            root.get<TopicEntity>("topic").get<String>("key").`in`(topicKeys)
        }

    fun search(term: String, searchInAnswers: Boolean): Specification<QuestionEntity> =
        Specification { root, query, cb ->
            if (query != null && query.resultType != Long::class.javaObjectType && query.resultType != Long::class.javaPrimitiveType) {
                query.distinct(true)
            }
            val pattern = "%${term.lowercase()}%"
            val promptMatch = cb.like(cb.lower(root.get("prompt")), pattern)

            if (!searchInAnswers) {
                promptMatch
            } else {
                val interviewJoin = root.join<QuestionEntity, InterviewContentEntity>("interviewContent", JoinType.LEFT)
                val quizJoin = root.join<QuestionEntity, QuizContentEntity>("quizContent", JoinType.LEFT)
                val quizAnswersJoin = quizJoin.join<QuizContentEntity, QuizAnswerEntity>("answers", JoinType.LEFT)

                cb.or(
                    promptMatch,
                    cb.like(cb.lower(interviewJoin.get("shortAnswer")), pattern),
                    cb.like(cb.lower(interviewJoin.get("longAnswer")), pattern),
                    cb.like(cb.lower(quizAnswersJoin.get("text")), pattern)
                )
            }
        }
}