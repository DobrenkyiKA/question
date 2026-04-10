package com.kdob.piq.content.infrastructure.persistence.question

import com.kdob.piq.content.infrastructure.persistence.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "interview_content")
class InterviewContentEntity(

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    val question: QuestionEntity,

    @Column(nullable = false)
    var shortAnswer: String,

    @Column
    var longAnswer: String?
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "interview_content_sequence")
    @SequenceGenerator(name = "interview_content_sequence", sequenceName = "interview_content_id_sequence", allocationSize = 50)
    var id: Long? = null

    override fun getIdValue(): Long? {
        return question.id
    }
}