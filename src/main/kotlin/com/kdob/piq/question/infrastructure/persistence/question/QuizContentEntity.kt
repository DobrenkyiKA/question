package com.kdob.piq.question.infrastructure.persistence.question

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "quiz_content")
class QuizContentEntity(

    @Id
    @GeneratedValue
    val id: UUID? = null,

    @OneToOne
    @JoinColumn(name = "question_id", unique = true, nullable = false)
    val question: QuestionEntity,

    @OneToMany(
        mappedBy = "quizContent",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val answers: MutableList<QuizAnswerEntity> = mutableListOf()
)