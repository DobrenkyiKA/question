package com.kdob.piq.question.persistence

import com.kdob.piq.question.domain.quesiton.Difficulty
import com.kdob.piq.question.persistence.topic.TopicEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "questions")
class QuestionEntity {
    @Id
    @GeneratedValue
    val id: UUID? = null

    @Column(nullable = false, unique = true)
    lateinit var key: String

    @Column(nullable = false)
    lateinit var prompt: String

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    lateinit var difficulty: Difficulty

    @ElementCollection
    @CollectionTable(
        name = "question_labels",
        joinColumns = [JoinColumn(name = "question_id")]
    )
    @Column(name = "label", nullable = false)
    var labels: Set<String> = emptySet()

    @ManyToMany
    @JoinTable(
        name = "question_topics",
        joinColumns = [JoinColumn(name = "question_id")],
        inverseJoinColumns = [JoinColumn(name = "topic_id")]
    )
    var topics: Set<TopicEntity> = emptySet()

    @OneToOne(mappedBy = "question", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var interviewContent: InterviewContentEntity? = null

    @OneToOne(mappedBy = "question", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var quizContent: QuizContentEntity? = null
}