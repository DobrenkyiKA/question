package com.kdob.piq.question.api.question.prompt

import com.kdob.piq.question.api.dto.question.prompt.QuestionPromptResponse
import com.kdob.piq.question.application.question.prompt.QuestionPromptQueryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/question-prompts")
class InternalQuestionPromptController(
    private val queryService: QuestionPromptQueryService
) {
    @GetMapping
    fun getQuestionPrompts(
        @RequestParam topicKeys: Set<String>
    ): List<QuestionPromptResponse> {
        return queryService.findPromptsByTopicKeys(topicKeys)
    }
}