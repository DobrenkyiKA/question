package com.kdob.piq.question.infrastructure.persistence.question

import com.kdob.piq.question.domain.question.Difficulty
import com.kdob.piq.question.infrastructure.persistence.topic.TopicEntity
import jakarta.persistence.*
import java.util.*

@Entity(name = "Question")
@Table(name = "questions")
class QuestionEntity(

    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false, unique = true)
    val key: String,

    @Column(nullable = false)
    val prompt: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val difficulty: Difficulty,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    val topic: TopicEntity,

    @ElementCollection
    @CollectionTable(
        name = "question_labels",
        joinColumns = [JoinColumn(name = "question_id")]
    )
    @Column(name = "label", nullable = false)
    val labels: Set<String> = emptySet(),

    @OneToOne(mappedBy = "question", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var interviewContent: InterviewContentEntity? = null,

    @OneToOne(mappedBy = "question", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var quizContent: QuizContentEntity? = null
)