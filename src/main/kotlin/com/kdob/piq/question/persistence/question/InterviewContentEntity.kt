package com.kdob.piq.question.persistence

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "interview_content")
class InterviewContentEntity(

    @Id
    val questionId: UUID,

    @MapsId
    @OneToOne
    @JoinColumn(name = "question_id")
    val question: QuestionEntity,

    @Column(nullable = false)
    val shortAnswer: String,

    @Column
    val longAnswer: String?
)