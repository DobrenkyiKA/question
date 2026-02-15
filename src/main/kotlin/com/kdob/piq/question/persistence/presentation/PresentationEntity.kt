package com.kdob.piq.question.persistence.presentation

import com.kdob.piq.question.domain.presentation.PresentationFormat
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "presentations")
class PresentationEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(name = "learning_item_id", nullable = false)
    val learningItemId: UUID,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val format: PresentationFormat,

    @Column(nullable = false)
    val prompt: String
)