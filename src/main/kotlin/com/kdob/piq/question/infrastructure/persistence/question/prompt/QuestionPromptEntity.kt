package com.kdob.piq.question.infrastructure.persistence.question.prompt

import com.kdob.piq.question.infrastructure.persistence.BaseEntity
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
    @Column(nullable = false)
    val prompt: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    val topic: TopicEntity
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "questions_sequence")
    @SequenceGenerator(name = "questions_sequence", sequenceName = "questions_id_sequence", allocationSize = 50)
    var id: Long? = null
    override fun getIdValue(): Long? {
        return id
    }
}
