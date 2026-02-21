package com.kdob.piq.question.persistence

import com.kdob.piq.question.domain.quesiton.Difficulty
import com.kdob.piq.question.persistence.topic.TopicEntity
import jakarta.persistence.criteria.JoinType
import org.springframework.data.jpa.domain.Specification
import java.util.*

object QuestionSpecifications {

    fun hasDifficulty(difficulties: Set<Difficulty>): Specification<QuestionEntity> =
        Specification { root, _, cb ->
            root.get<Difficulty>("difficulty").`in`(difficulties)
        }

    fun hasAnyLabel(labels: Set<String>): Specification<QuestionEntity> =
        Specification { root, _, cb ->
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
        Specification { root, _, cb ->
            root.get<TopicEntity>("topic").get<UUID>("id").`in`(topicIds)
        }
}