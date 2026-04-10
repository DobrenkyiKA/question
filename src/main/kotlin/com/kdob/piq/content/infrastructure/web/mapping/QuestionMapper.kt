package com.kdob.piq.content.infrastructure.web.mapping

import com.kdob.piq.content.infrastructure.web.dto.question.InterviewContentResponse
import com.kdob.piq.content.infrastructure.web.dto.question.QuestionResponse
import com.kdob.piq.content.infrastructure.web.dto.question.QuizAnswerResponse
import com.kdob.piq.content.infrastructure.web.dto.question.QuizContentResponse
import com.kdob.piq.content.domain.question.InterviewContent
import com.kdob.piq.content.domain.question.Question
import com.kdob.piq.content.domain.question.QuizAnswer
import com.kdob.piq.content.domain.question.QuizContent

object QuestionMapper {

    fun toResponse(domain: Question): QuestionResponse =
        QuestionResponse(
            key = domain.key,
            prompt = domain.prompt,
            difficulty = domain.difficulty,
            labels = domain.labels,
            topic = domain.topicKey,
            interview = domain.interviewContent?.let { toInterviewResponse(it) },
            quiz = domain.quizContent?.let { toQuizResponse(it) }
        )

    private fun toInterviewResponse(
        domain: InterviewContent
    ): InterviewContentResponse =
        InterviewContentResponse(
            shortAnswer = domain.shortAnswer,
            longAnswer = domain.longAnswer
        )

    private fun toQuizResponse(
        domain: QuizContent
    ): QuizContentResponse =
        QuizContentResponse(
            answers = domain.answers.map { toQuizAnswerResponse(it) }
        )

    private fun toQuizAnswerResponse(
        domain: QuizAnswer
    ): QuizAnswerResponse =
        QuizAnswerResponse(
            text = domain.text,
            correct = domain.correct,
            explanation = domain.explanation
        )
}