package com.kdob.piq.question.infrastructure.persistence.question.prompt

import com.kdob.piq.question.infrastructure.persistence.topic.TopicEntity
import jakarta.persistence.*
import java.util.*

/**
 * Read-only projection entity used ONLY
 * for AI pipeline deduplication.
 *
 * Maps to the same table as QuestionEntity,
 * but exposes only the required fields.
 */
@Entity
@Table(name = "questions")
class QuestionPromptEntity(
    @Id
    val id: UUID,

    @Column(nullable = false)
    val prompt: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    val topic: TopicEntity
)
