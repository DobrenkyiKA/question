package com.kdob.piq.question.persistence.progress

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "learning_progress")
@IdClass(LearningProgressId::class)
class LearningProgressEntity(

    @Id
    @Column(name = "user_id")
    val userId: UUID,

    @Id
    @Column(name = "learning_item_id")
    val learningItemId: UUID,

    @Column(name = "learned_at")
    val learnedAt: Instant?
)