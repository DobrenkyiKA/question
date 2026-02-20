package com.kdob.piq.question.config

import com.kdob.piq.question.domain.quesiton.Difficulty
import com.kdob.piq.question.persistence.*
import com.kdob.piq.question.persistence.topic.SpringDataTopicRepository
import com.kdob.piq.question.persistence.topic.TopicEntity
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.util.*

/**
 * Development initializer: seeds the database with a nested structure of Topics and Questions.
 */
@Component
@Profile(value = ["default", "dev"])
class DataInitializer(
    private val topicRepository: SpringDataTopicRepository,
    private val questionRepository: QuestionRepository
) : CommandLineRunner {

    private val log = LoggerFactory.getLogger(DataInitializer::class.java)

    override fun run(vararg args: String) {
        if (topicRepository.count() > 0) {
            log.info("[DataInitializer] Database already seeded. Skipping.")
            return
        }

        log.info("[DataInitializer] Seeding database with topics and questions...")

        val topLevels = listOf("Java", "Kotlin", "Building tools", "Microservices")

        topLevels.forEach { rootName ->
            val rootKey = rootName.lowercase().replace(" ", "-")
            val rootTopic = createTopic(rootName, rootKey, null, "/$rootKey")

            // Generate at least 10 descendants with nesting >= 6 (we'll make a chain of 11 deep including root)
            var currentParent = rootTopic
            for (i in 1..10) {
                val subKeyPart = "sub-$i"
                val subKey = "${currentParent.key}-$subKeyPart" // ensure globally unique key
                val name = "$rootName Sub $i"
                val path = "${currentParent.path}/$subKey" // path uses child's key segment like AdminTopicService
                currentParent = createTopic(name, subKey, currentParent, path)
            }

            // For the leaf topic, generate at least 10 questions
            seedQuestions(currentParent)
        }

        log.info("[DataInitializer] Seeding completed.")
    }

    private fun createTopic(name: String, key: String, parent: TopicEntity?, path: String): TopicEntity {
        val topic = TopicEntity(
            key = key,
            name = name,
            parentId = parent?.id,
            path = path
        )
        val saved = topicRepository.save(topic)
        log.debug("[DataInitializer] Created topic: {}", saved.path)
        return saved
    }

    private fun seedQuestions(topic: TopicEntity) {
        val questions = mutableListOf<QuestionEntity>()
        val difficulties = Difficulty.values()

        for (i in 1..10) {
            val questionKey = "${topic.key}-q-$i"
            val question = QuestionEntity().apply {
                this.key = questionKey
                this.prompt = "What is ${topic.name} concept #$i?"
                this.difficulty = difficulties[i % difficulties.size]
                this.topics = setOf(topic)
                this.labels = setOf("seeded", topic.key)
            }

            val interviewContent = InterviewContentEntity(
                questionId = UUID.randomUUID(), // Placeholder, @MapsId will override
                question = question,
                shortAnswer = "This is a concise short answer for $questionKey.",
                longAnswer = "This is a detailed long answer for $questionKey. It provides comprehensive information about the topic and explains all the nuances."
            )
            question.interviewContent = interviewContent

            val answersList = mutableListOf<QuizAnswerEntity>()
            val quizContent = QuizContentEntity(
                question = question,
                answers = answersList
            )

            val numAnswers = (5..7).random()
            for (j in 1..numAnswers) {
                answersList.add(
                    QuizAnswerEntity(
                        quizContent = quizContent,
                        text = "Answer option $j for $questionKey",
                        correct = (j == 1),
                        explanation = if (j == 1) "Correct! This is the right answer." else "Incorrect. This is a common mistake."
                    )
                )
            }
            question.quizContent = quizContent

            questions.add(question)
        }

        questionRepository.saveAll(questions)
        log.info("[DataInitializer] Seeded 10 questions for topic: {}", topic.path)
    }
}
