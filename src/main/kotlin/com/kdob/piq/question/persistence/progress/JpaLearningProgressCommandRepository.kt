package com.kdob.piq.question.persistence.progress

import com.kdob.piq.question.domain.progress.LearningProgressCommandRepository
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.UUID
import java.time.Instant

@Repository
class JpaLearningProgressCommandRepository(
    private val jdbc: NamedParameterJdbcTemplate
) : LearningProgressCommandRepository {

    override fun markLearned(
        userId: UUID,
        learningItemId: UUID,
        at: Instant
    ) {
        jdbc.update(
            """
            INSERT INTO learning_progress (user_id, learning_item_id, learned_at)
            VALUES (:u, :i, :t)
            ON CONFLICT (user_id, learning_item_id)
            DO UPDATE SET learned_at = :t
            """,
            mapOf("u" to userId, "i" to learningItemId, "t" to at)
        )
    }
}