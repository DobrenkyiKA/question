package com.kdob.piq.question.api.mapping

import com.kdob.piq.question.api.dto.question.InterviewContentResponse
import com.kdob.piq.question.api.dto.question.QuestionResponse
import com.kdob.piq.question.api.dto.question.QuizAnswerResponse
import com.kdob.piq.question.api.dto.question.QuizContentResponse
import com.kdob.piq.question.persistence.InterviewContentEntity
import com.kdob.piq.question.persistence.QuestionEntity
import com.kdob.piq.question.persistence.QuizAnswerEntity
import com.kdob.piq.question.persistence.QuizContentEntity

object QuestionMapper {

    fun toResponse(entity: QuestionEntity): QuestionResponse =
        QuestionResponse(
            id = entity.id!!,
            key = entity.key,
            prompt = entity.prompt,
            difficulty = entity.difficulty,
            labels = entity.labels.toSet(),
            topic = entity.topic.key,
            interview = entity.interviewContent?.let { toInterviewResponse(it) },
            quiz = entity.quizContent?.let { toQuizResponse(it) }
        )

    private fun toInterviewResponse(
        entity: InterviewContentEntity
    ): InterviewContentResponse =
        InterviewContentResponse(
            shortAnswer = entity.shortAnswer,
            longAnswer = entity.longAnswer
        )

    private fun toQuizResponse(
        entity: QuizContentEntity
    ): QuizContentResponse =
        QuizContentResponse(
            answers = entity.answers.map { toQuizAnswerResponse(it) }
        )

    private fun toQuizAnswerResponse(
        entity: QuizAnswerEntity
    ): QuizAnswerResponse =
        QuizAnswerResponse(
            text = entity.text,
            correct = entity.correct,
            explanation = entity.explanation
        )
}