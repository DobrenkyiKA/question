package com.kdob.piq.question.api

import com.kdob.piq.question.api.dto.PresentationResponse
import com.kdob.piq.question.api.mapping.toResponse
import com.kdob.piq.question.api.security.currentUserId
import com.kdob.piq.question.application.LearningService
import com.kdob.piq.question.domain.learning.LearningState
import com.kdob.piq.question.domain.presentation.PresentationFormat
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/learning")
class LearningController(
    private val service: LearningService
) {

    @GetMapping("/topics")
    fun getItems(
        @RequestParam topicPath: String,
        @RequestParam format: PresentationFormat,
        @RequestParam state: LearningState = LearningState.ANY,
        @RequestParam limit: Int = 10
    ): List<PresentationResponse> =
        service.getPresentations(
            topicPath = topicPath,
            format = format,
            state = state,
            limit = limit,
            userId = currentUserId()
        ).map { it.toResponse() }

    @PostMapping("/{id}/learned")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun markLearned(@PathVariable id: UUID) {
        service.markLearned(currentUserId(), id)
    }
}