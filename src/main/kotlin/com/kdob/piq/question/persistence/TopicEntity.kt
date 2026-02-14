package com.kdob.piq.question.persistence

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "topics")
class TopicEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false)
    val name: String,

    @Column(name = "parent_id")
    val parentId: UUID?
)