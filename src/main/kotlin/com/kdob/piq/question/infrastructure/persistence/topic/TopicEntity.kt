package com.kdob.piq.question.infrastructure.persistence.topic

import jakarta.persistence.*
import com.kdob.piq.question.infrastructure.persistence.BaseEntity
import com.kdob.piq.question.infrastructure.persistence.question.QuestionEntity

@Entity
@Table(name = "topics")
class TopicEntity(
    @Column(nullable = false, unique = true)
    var key: String,

    @Column(nullable = false)
    var name: String,

    @Column(name = "parent_id")
    var parentId: Long?,

    @Column(nullable = false, unique = true)
    var path: String,

    @Column(nullable = false)
    var description: String = ""
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "topics_sequence")
    @SequenceGenerator(name = "topics_sequence", sequenceName = "topics_id_sequence", allocationSize = 50)
    var id: Long? = null

    @OneToMany(mappedBy = "topic", cascade = [CascadeType.ALL], orphanRemoval = true)
    var questions: MutableSet<QuestionEntity> = mutableSetOf()

    override fun getIdValue(): Long? {
        return id
    }
}