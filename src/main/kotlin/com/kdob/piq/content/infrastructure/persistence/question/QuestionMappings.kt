package com.kdob.piq.content.infrastructure.persistence.question

import com.kdob.piq.content.domain.question.InterviewContent
import com.kdob.piq.content.domain.question.Question
import com.kdob.piq.content.domain.question.QuizAnswer
import com.kdob.piq.content.domain.question.QuizContent

fun QuestionEntity.toDomain(): Question =
    Question(
        key = key,
        prompt = prompt,
        difficulty = difficulty,
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
