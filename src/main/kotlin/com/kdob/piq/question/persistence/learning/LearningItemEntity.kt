package com.kdob.piq.question.persistence.learning

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "learning_items")
class LearningItemEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null
)