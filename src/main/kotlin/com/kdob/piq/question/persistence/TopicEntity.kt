package com.kdob.piq.question.persistence

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "topics")
class TopicEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false, unique = true)
    val name: String = ""
)