package com.kdob.piq.question.persistence.question.prompt

import com.kdob.piq.question.persistence.topic.TopicEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.UUID

/**
 * Read-only projection entity used ONLY
 * for AI pipeline deduplication.
 *
 * Maps to the same table as QuestionEntity,
 * but exposes only the required fields.
 */
@Entity
@Table(name = "questions")
class QuestionPromptEntity (
    @Id
    val id: UUID,

    @Column(nullable = false)
    val prompt: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    val topic: TopicEntity
    )
