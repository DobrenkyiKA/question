package com.kdob.piq.question.persistence.topic

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "topics")
class TopicEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false, unique = true)
    val key: String,

    @Column(nullable = false)
    val name: String,

    @Column(name = "parent_id")
    val parentId: UUID?,

    @Column(nullable = false, unique = true)
    val path: String
)