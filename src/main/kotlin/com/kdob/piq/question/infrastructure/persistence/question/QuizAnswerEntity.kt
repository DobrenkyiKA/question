package com.kdob.piq.question.infrastructure.persistence.question

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "quiz_answers")
class QuizAnswerEntity(

    @Id
    @GeneratedValue
    val id: UUID? = null,

    @ManyToOne
    @JoinColumn(name = "quiz_content_id", nullable = false)
    val quizContent: QuizContentEntity,

    @Column(nullable = false)
    val text: String,

    @Column(nullable = false)
    val correct: Boolean,

    @Column
    val explanation: String?
)