package com.kdob.piq.question.infrastructure.persistence.question

import com.kdob.piq.question.domain.question.Difficulty
import com.kdob.piq.question.infrastructure.persistence.BaseEntity
import com.kdob.piq.question.infrastructure.persistence.topic.TopicEntity
import jakarta.persistence.*
import java.util.*

@Entity(name = "Question")
@Table(name = "questions")
class QuestionEntity(

    @Column(nullable = false, unique = true)
    val key: String,

    @Column(nullable = false)
    var prompt: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var difficulty: Difficulty,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    var topic: TopicEntity,

    @ElementCollection
    @CollectionTable(
        name = "question_labels",
        joinColumns = [JoinColumn(name = "question_id")]
    )
    @Column(name = "label", nullable = false)
    var labels: Set<String> = emptySet(),

    @OneToOne(mappedBy = "question", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var interviewContent: InterviewContentEntity? = null,

    @OneToOne(mappedBy = "question", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var quizContent: QuizContentEntity? = null
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "questions_sequence")
    @SequenceGenerator(name = "questions_sequence", sequenceName = "questions_id_sequence", allocationSize = 50)
    var id: Long? = null
    override fun getIdValue(): Long? {
        return id
    }
}