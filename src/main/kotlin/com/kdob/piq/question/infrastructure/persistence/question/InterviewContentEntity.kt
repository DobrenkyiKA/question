package com.kdob.piq.question.infrastructure.persistence.question

import com.kdob.piq.question.infrastructure.persistence.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "interview_content")
class InterviewContentEntity(


    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    val question: QuestionEntity,

    @Column(nullable = false)
    val shortAnswer: String,

    @Column
    val longAnswer: String?
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "interview_content_sequence")
    @SequenceGenerator(name = "interview_content_sequence", sequenceName = "interview_content_id_sequence", allocationSize = 50)
    var id: Long? = null

    override fun getIdValue(): Long? {
        return question.id
    }
}