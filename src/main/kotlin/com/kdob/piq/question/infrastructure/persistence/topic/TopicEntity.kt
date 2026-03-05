package com.kdob.piq.question.infrastructure.persistence.topic

import jakarta.persistence.*
import com.kdob.piq.question.infrastructure.persistence.BaseEntity

@Entity
@Table(name = "topics")
class TopicEntity(
    @Column(nullable = false, unique = true)
    val key: String,

    @Column(nullable = false)
    val name: String,

    @Column(name = "parent_id")
    val parentId: Long?,

    @Column(nullable = false, unique = true)
    val path: String
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "topics_sequence")
    @SequenceGenerator(name = "topics_sequence", sequenceName = "topics_id_sequence", allocationSize = 50)
    var id: Long? = null
    override fun getIdValue(): Long? {
        return id
    }
}