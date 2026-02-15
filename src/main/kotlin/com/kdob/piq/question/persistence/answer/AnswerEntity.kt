package com.kdob.piq.question.persistence.answer

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "answers")
class AnswerEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(name = "presentation_id", nullable = false)
    val presentationId: UUID,

    @Column(nullable = false)
    val text: String,

    @Column(nullable = false)
    val correct: Boolean,

    @Column
    val explanation: String?
)