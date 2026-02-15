package com.kdob.piq.question.persistence.learning

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SpringDataLearningItemRepository : JpaRepository<LearningItemEntity, UUID>