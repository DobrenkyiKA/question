package com.kdob.piq.question.domain.presentation

interface PresentationCommandRepository {
    fun save(presentation: Presentation): Presentation
}