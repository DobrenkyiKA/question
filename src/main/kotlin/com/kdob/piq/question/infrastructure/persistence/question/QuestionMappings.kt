package com.kdob.piq.question.infrastructure.persistence.question

import com.kdob.piq.question.domain.question.InterviewContent
import com.kdob.piq.question.domain.question.Question
import com.kdob.piq.question.domain.question.QuizAnswer
import com.kdob.piq.question.domain.question.QuizContent

fun QuestionEntity.toDomain(): Question =
    Question(
        id = id,
        key = key,
        prompt = prompt,
        difficulty = difficulty,
        topicId = topic.id!!,
        topicKey = topic.key,
        labels = labels.toSet(),
        interviewContent = interviewContent?.toDomain(),
        quizContent = quizContent?.toDomain()
    )

fun InterviewContentEntity.toDomain(): InterviewContent =
    InterviewContent(
        shortAnswer = shortAnswer,
        longAnswer = longAnswer
    )

fun QuizContentEntity.toDomain(): QuizContent =
    QuizContent(
        answers = answers.map { it.toDomain() }
    )

fun QuizAnswerEntity.toDomain(): QuizAnswer =
    QuizAnswer(
        text = text,
        correct = correct,
        explanation = explanation
    )
