package com.kdob.piq.question.api.question

import com.kdob.piq.question.api.dto.question.QuestionResponse
import com.kdob.piq.question.application.question.QuestionQueryService
import com.kdob.piq.question.domain.quesiton.Difficulty
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/questions")
class QuestionController(
    private val service: QuestionQueryService
) {

    @GetMapping
    fun getQuestions(
        @RequestParam(required = false) topicKeys: Set<String>?,
        @RequestParam(required = false) difficulty: Set<Difficulty>?,
        @RequestParam(required = false) labels: Set<String>?,
        @RequestParam(required = false) formats: Set<String>?,
        pageable: Pageable
    ): Page<QuestionResponse> {
        return service.findQuestions(
            topicKeys = topicKeys ?: emptySet(),
            difficulties = difficulty ?: emptySet(),
            labels = labels ?: emptySet(),
            formats = formats ?: emptySet(),
            pageable = pageable
        )
    }
}