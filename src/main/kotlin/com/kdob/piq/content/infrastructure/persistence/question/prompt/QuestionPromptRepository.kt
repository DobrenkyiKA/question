package com.kdob.piq.content.infrastructure.persistence.question.prompt

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface QuestionPromptRepository :
    JpaRepository<QuestionPromptEntity, Long> {

    @Query(
        """
        select q
        from QuestionPromptEntity q
        join fetch q.topic t
        where t.key in :topicKeys
    """
    )
    fun findAllByTopicKeys(topicKeys: Set<String>): List<QuestionPromptEntity>
}