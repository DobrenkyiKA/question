package com.kdob.piq.content.infrastructure.persistence.question

import com.kdob.piq.content.infrastructure.persistence.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "quiz_answers")
class QuizAnswerEntity(

    @ManyToOne
    @JoinColumn(name = "quiz_content_id", nullable = false)
    val quizContent: QuizContentEntity,

    @Column(nullable = false)
    var text: String,

    @Column(nullable = false)
    var correct: Boolean,

    @Column
    var explanation: String?
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_answers_sequence")
    @SequenceGenerator(name = "quiz_answers_sequence", sequenceName = "quiz_answers_id_sequence", allocationSize = 50)
    var id: Long? = null

    override fun getIdValue(): Long? {
        return id
    }
}