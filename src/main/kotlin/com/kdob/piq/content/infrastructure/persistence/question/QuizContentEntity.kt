package com.kdob.piq.content.infrastructure.persistence.question

import com.kdob.piq.content.infrastructure.persistence.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "quiz_content")
class QuizContentEntity(

    @OneToOne
    @JoinColumn(name = "question_id", unique = true, nullable = false)
    val question: QuestionEntity,

    @OneToMany(
        mappedBy = "quizContent",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val answers: MutableList<QuizAnswerEntity> = mutableListOf()
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_content_sequence")
    @SequenceGenerator(name = "quiz_content_sequence", sequenceName = "quiz_content_id_sequence", allocationSize = 50)
    var id: Long? = null
    override fun getIdValue(): Long? {
        return id
    }
}