package com.kdob.piq.question.infrastructure.persistence.question

import com.kdob.piq.question.domain.question.Difficulty
import com.kdob.piq.question.infrastructure.persistence.topic.TopicEntity
import jakarta.persistence.criteria.JoinType
import org.springframework.data.jpa.domain.Specification
import java.util.*

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

    fun hasAnyTopicId(topicIds: Set<UUID>): Specification<QuestionEntity> =
        Specification { root, _, _ ->
            root.get<TopicEntity>("topic").get<UUID>("id").`in`(topicIds)
        }
}