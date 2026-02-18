package com.kdob.piq.question.persistence.learning

import com.kdob.piq.question.domain.learning.LearningItem
import com.kdob.piq.question.domain.learning.LearningItemCommandRepository
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class JpaLearningItemCommandRepository(
    private val repo: SpringDataLearningItemRepository,
    private val jdbc: NamedParameterJdbcTemplate
) : LearningItemCommandRepository {

    override fun save(item: LearningItem): LearningItem =
        repo.saveAndFlush(item.toEntity()).toDomain()


    override fun attachToTopics(itemId: UUID, topicIds: List<UUID>) {
        val sql = "INSERT INTO learning_item_topics (learning_item_id, topic_id) VALUES (:itemId, :topicId)"

        topicIds.forEach { jdbc.update(sql, mapOf("itemId" to itemId, "topicId" to it)) }
    }
}
