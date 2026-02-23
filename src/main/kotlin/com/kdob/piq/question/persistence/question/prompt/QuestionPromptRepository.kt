package com.kdob.piq.question.persistence.question.prompt

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface QuestionPromptRepository :
    JpaRepository<QuestionPromptEntity, UUID> {

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