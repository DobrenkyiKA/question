package com.kdob.piq.question.persistence.learning

import com.kdob.piq.question.domain.answer.Answer
import com.kdob.piq.question.domain.learning.LearningQueryRepository
import com.kdob.piq.question.domain.learning.LearningState
import com.kdob.piq.question.domain.presentation.Presentation
import com.kdob.piq.question.domain.presentation.PresentationFormat
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class JpaLearningQueryRepository(
    private val jdbc: NamedParameterJdbcTemplate
) : LearningQueryRepository {

    override fun findPresentations(
        topicPath: String,
        format: PresentationFormat,
        state: LearningState,
        userId: UUID,
        limit: Int
    ): List<Presentation> {

        val sql = """
            SELECT
              p.id                  AS presentation_id,
              p.learning_item_id    AS learning_item_id,
              p.format              AS format,
              p.prompt              AS prompt,

              a.id                  AS answer_id,
              a.text                AS answer_text,
              a.correct             AS answer_correct,
              a.explanation         AS answer_explanation
            FROM topics t
            JOIN learning_item_topics lit
              ON lit.topic_id = t.id
            JOIN learning_items li
              ON li.id = lit.learning_item_id
            JOIN presentations p
              ON p.learning_item_id = li.id
            JOIN answers a
              ON a.presentation_id = p.id
            LEFT JOIN learning_progress lp
              ON lp.learning_item_id = li.id
             AND lp.user_id = :userId
            WHERE t.path LIKE :pathPrefix
              AND p.format = :format
              AND (
                   :state = 'ANY'
                OR (:state = 'NEW' AND lp.learning_item_id IS NULL)
                OR (:state = 'LEARNED' AND lp.learned_at IS NOT NULL)
              )
            ORDER BY random()
            LIMIT :limit
        """

        val params = mapOf(
            "pathPrefix" to "$topicPath%",
            "format" to format.name,
            "state" to state.name,
            "userId" to userId,
            "limit" to limit
        )

        val rows = jdbc.query(sql, params) { rs, _ ->
            Row(
                presentationId = rs.getObject("presentation_id", UUID::class.java),
                learningItemId = rs.getObject("learning_item_id", UUID::class.java),
                format = PresentationFormat.valueOf(rs.getString("format")),
                prompt = rs.getString("prompt"),
                answer = Answer(
                    id = rs.getObject("answer_id", UUID::class.java),
                    text = rs.getString("answer_text"),
                    correct = rs.getBoolean("answer_correct"),
                    explanation = rs.getString("answer_explanation")
                )
            )
        }

        return rows
            .groupBy { it.presentationId }
            .map { (_, grouped) ->
                val first = grouped.first()
                Presentation(
                    id = first.presentationId,
                    learningItemId = first.learningItemId,
                    format = first.format,
                    prompt = first.prompt,
                    answers = grouped.map { it.answer }
                )
            }
    }

    private data class Row(
        val presentationId: UUID,
        val learningItemId: UUID,
        val format: PresentationFormat,
        val prompt: String,
        val answer: Answer
    )
}