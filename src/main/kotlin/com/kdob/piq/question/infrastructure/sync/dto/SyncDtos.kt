package com.kdob.piq.question.infrastructure.sync.dto

data class RootTopicDto(
    val rootTopicKey: String,
    val topics: List<TopicDto>
)

data class TopicDto(
    val key: String,
    val name: String,
    val path: String,
    val parentKey: String?,
    val coverageArea: String = "",
    val exclusions: String = ""
)

data class TopicQuestionsDto(
    val topicKey: String,
    val questions: List<QuestionDto>
)

data class QuestionDto(
    val key: String,
    val prompt: String,
    val difficulty: String,
    val labels: List<String>,
    val interviewContent: InterviewContentDto?,
    val quizContent: QuizContentDto?
)

data class InterviewContentDto(
    val shortAnswer: String?,
    val longAnswer: String?
)

data class QuizContentDto(
    val answers: List<QuizAnswerDto>
)

data class QuizAnswerDto(
    val text: String,
    val correct: Boolean,
    val explanation: String?
)
