package com.kdob.piq.question.infrastructure.persistence.topic

import jakarta.persistence.*
import com.kdob.piq.question.infrastructure.persistence.BaseEntity
import com.kdob.piq.question.infrastructure.persistence.question.QuestionEntity
import org.hibernate.annotations.BatchSize

@Entity
@Table(name = "topics")
class TopicEntity(
    @Column(nullable = false, unique = true)
    var key: String,

    @Column(nullable = false)
    var name: String,

    @Column(name = "parent_id")
    var parentId: Long?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    var parent: TopicEntity? = null,

    @BatchSize(size = 50)
    @OneToMany(mappedBy = "parent", cascade = [CascadeType.ALL], orphanRemoval = true)
    var children: MutableSet<TopicEntity> = mutableSetOf(),

    @Column(nullable = false, unique = true)
    var path: String,

    @Column(name = "coverage_area", nullable = false)
    var coverageArea: String = "",
    @Column(nullable = false)
    var exclusions: String = ""
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "topics_sequence")
    @SequenceGenerator(name = "topics_sequence", sequenceName = "topics_id_sequence", allocationSize = 50)
    var id: Long? = null

    @BatchSize(size = 50)
    @OneToMany(mappedBy = "topic", cascade = [CascadeType.ALL], orphanRemoval = true)
    var questions: MutableSet<QuestionEntity> = mutableSetOf()

    override fun getIdValue(): Long? {
        return id
    }
}