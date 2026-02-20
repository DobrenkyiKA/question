package com.kdob.piq.question.persistence

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.util.UUID

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
    val answers: List<QuizAnswerEntity> = emptyList()
)