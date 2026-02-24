package com.kdob.piq.question.application.service

import com.kdob.piq.question.infrastructure.web.dto.question.prompt.QuestionPromptResponse
import com.kdob.piq.question.infrastructure.persistence.question.prompt.QuestionPromptRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class QuestionPromptQueryService(
    private val repository: QuestionPromptRepository
) {

    /**
     * Returns question prompts that belong
     * directly to the specified topic keys.
     *
     * Used by AI pipeline to prevent duplication.
     */
    @Transactional(readOnly = true)
    fun findPromptsByTopicKeys(
        topicKeys: Set<String>
    ): List<QuestionPromptResponse> {

        if (topicKeys.isEmpty()) {
            return emptyList()
        }

        return repository
            .findAllByTopicKeys(topicKeys)
            .map {
                QuestionPromptResponse(
                    prompt = it.prompt,
                    topicKey = it.topic.key
                )
            }
            .distinctBy { it.prompt.trim().lowercase() }
    }
}