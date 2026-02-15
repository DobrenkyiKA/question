package com.kdob.piq.question.persistence.learning

import com.kdob.piq.question.domain.learning.LearningItem
import com.kdob.piq.question.domain.learning.LearningItemQueryRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class JpaLearningItemQueryRepository(
    private val repo: SpringDataLearningItemRepository
) : LearningItemQueryRepository{

    override fun findById(id: UUID): LearningItem? =
        repo.findById(id).map { it.toDomain() }.orElse(null)
}