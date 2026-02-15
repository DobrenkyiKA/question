package com.kdob.piq.question.domain.presentation

import java.util.*

interface PresentationQueryRepository {
    fun findById(id: UUID): Presentation?
}