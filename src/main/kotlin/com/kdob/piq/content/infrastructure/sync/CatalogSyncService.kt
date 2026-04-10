package com.kdob.piq.content.infrastructure.sync

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.kdob.piq.content.domain.question.Difficulty
import com.kdob.piq.content.infrastructure.client.StorageServiceClient
import com.kdob.piq.content.infrastructure.persistence.question.*
import com.kdob.piq.content.infrastructure.persistence.topic.SpringDataTopicRepository
import com.kdob.piq.content.infrastructure.persistence.topic.TopicEntity
import com.kdob.piq.content.infrastructure.sync.dto.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CatalogSyncService(
    private val topicRepository: SpringDataTopicRepository,
    private val questionRepository: QuestionRepository,
    private val storageServiceClient: StorageServiceClient
) {
    private val yamlMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

    @Transactional
    fun exportToVersion(version: String, commitMessage: String) {
        // Export Topics
        val roots = topicRepository.findByParentIdIsNull()
        roots.forEach { root ->
            val allInRoot = topicRepository.findByPathStartingWith(root.path)
            val dto = RootTopicDto(
                rootTopicKey = root.key,
                topics = allInRoot.map { it.toDto() }
            )
            val content = yamlMapper.writeValueAsString(dto)
            storageServiceClient.saveTopicFile(version, "${root.key}.yaml", content)
        }

        // Export Questions
        val questions = questionRepository.findAll()
        val questionsByTopic = questions.groupBy { it.topic.key }
        questionsByTopic.forEach { (topicKey, topicQuestions) ->
            val dto = TopicQuestionsDto(
                topicKey = topicKey,
                questions = topicQuestions.map { it.toDto() }
            )
            val content = yamlMapper.writeValueAsString(dto)
            storageServiceClient.saveQuestionFile(version, "$topicKey.yaml", content)
        }

        storageServiceClient.commit(version, commitMessage)
    }

    @Transactional
    fun importFromVersion(version: String) {
        // Import Topics
        val topicFiles = storageServiceClient.listTopics(version)
        val allImportedTopics = mutableListOf<TopicDto>()
        topicFiles.forEach { fileName ->
            val content = storageServiceClient.getTopicFile(version, fileName)
            if (content != null) {
                val dto = yamlMapper.readValue(content, RootTopicDto::class.java)
                allImportedTopics.addAll(dto.topics)
            }
        }

        // Upsert topics (sorted by path depth)
        val sortedTopics = allImportedTopics.sortedBy { it.path.split("/").size }
        val importedTopicKeys = sortedTopics.map { it.key }.toSet()

        sortedTopics.forEach { dto ->
            val existing = topicRepository.findByKey(dto.key)
            val parentId = dto.parentKey?.let {
                topicRepository.findByKey(it)?.id ?: throw IllegalStateException("Parent topic $it not found for topic ${dto.key}")
            }
            if (existing != null) {
                existing.name = dto.name
                existing.path = dto.path
                existing.parentId = parentId
                existing.coverageArea = dto.coverageArea
                existing.exclusions = dto.exclusions
                topicRepository.save(existing)
            } else {
                val entity = TopicEntity(
                    key = dto.key,
                    name = dto.name,
                    path = dto.path,
                    parentId = parentId,
                    coverageArea = dto.coverageArea,
                    exclusions = dto.exclusions
                )
                topicRepository.save(entity)
            }
        }

        // Import Questions
        val questionFiles = storageServiceClient.listQuestions(version)
        val importedQuestionKeys = mutableSetOf<String>()
        questionFiles.forEach { fileName ->
            val content = storageServiceClient.getQuestionFile(version, fileName)
            if (content != null) {
                val dto = yamlMapper.readValue(content, TopicQuestionsDto::class.java)
                val topic = topicRepository.findByKey(dto.topicKey) ?: throw IllegalStateException("Topic ${dto.topicKey} not found for questions")
                dto.questions.forEach { qDto ->
                    importedQuestionKeys.add(qDto.key)
                    val existing = questionRepository.findByKey(qDto.key)
                    val difficulty = try { Difficulty.valueOf(qDto.difficulty) } catch (e: Exception) { Difficulty.MEDIUM }
                    
                    if (existing != null) {
                        existing.prompt = qDto.prompt
                        existing.difficulty = difficulty
                        existing.topic = topic
                        existing.labels = qDto.labels.toSet()
                        updateContents(existing, qDto)
                        questionRepository.save(existing)
                    } else {
                        val entity = QuestionEntity(
                            key = qDto.key,
                            prompt = qDto.prompt,
                            difficulty = difficulty,
                            topic = topic,
                            labels = qDto.labels.toSet()
                        )
                        updateContents(entity, qDto)
                        questionRepository.save(entity)
                    }
                }
            }
        }

        // Delete missing
        val allQuestions = questionRepository.findAll()
        allQuestions.filter { it.key !in importedQuestionKeys }.forEach {
            questionRepository.delete(it)
        }

        val allTopics = topicRepository.findAll()
        allTopics.filter { it.key !in importedTopicKeys }
            .sortedByDescending { it.path.split("/").size }
            .forEach {
                topicRepository.delete(it)
            }
    }

    @Transactional
    fun importArtifact(version: String, artifactYaml: String) {
        val artifact = yamlMapper.readValue(artifactYaml, Map::class.java)
        @Suppress("UNCHECKED_CAST")
        val topics = artifact["topics"] as? List<Map<String, Any>> ?: emptyList()

        topics.forEach { topicData ->
            val topicKey = topicData["key"] as String
            @Suppress("UNCHECKED_CAST")
            val questions = topicData["questions"] as? List<String> ?: emptyList()

            // Load existing file
            val fileName = "$topicKey.yaml"
            val existingContent = try { storageServiceClient.getQuestionFile(version, fileName) } catch (e: Exception) { null }
            val existingDto = existingContent?.let { yamlMapper.readValue(it, TopicQuestionsDto::class.java) }
                ?: TopicQuestionsDto(topicKey = topicKey, questions = emptyList())

            val updatedQuestions = existingDto.questions.toMutableList()
            questions.forEach { prompt ->
                // Basic check if it already exists
                if (updatedQuestions.none { it.prompt == prompt }) {
                    updatedQuestions.add(QuestionDto(
                        key = "$topicKey-q-${java.util.UUID.randomUUID().toString().take(8)}",
                        prompt = prompt,
                        difficulty = "MEDIUM",
                        labels = listOf("ai-generated"),
                        interviewContent = null,
                        quizContent = null
                    ))
                }
            }

            val newDto = existingDto.copy(questions = updatedQuestions)
            storageServiceClient.saveQuestionFile(version, fileName, yamlMapper.writeValueAsString(newDto))
        }

        storageServiceClient.commit(version, "Imported artifact from AI service")
        importFromVersion(version)
    }

    private fun updateContents(entity: QuestionEntity, dto: QuestionDto) {
        // Interview Content
        if (dto.interviewContent != null) {
            val ic = entity.interviewContent ?: InterviewContentEntity(
                question = entity,
                shortAnswer = dto.interviewContent.shortAnswer ?: "",
                longAnswer = dto.interviewContent.longAnswer
            )
            ic.shortAnswer = dto.interviewContent.shortAnswer ?: ""
            ic.longAnswer = dto.interviewContent.longAnswer
            entity.interviewContent = ic
        } else {
            entity.interviewContent = null
        }

        // Quiz Content
        if (dto.quizContent != null) {
            val qc = entity.quizContent ?: QuizContentEntity(
                question = entity
            )
            qc.answers.clear()
            dto.quizContent.answers.forEach { aDto ->
                qc.answers.add(QuizAnswerEntity(
                    quizContent = qc,
                    text = aDto.text,
                    correct = aDto.correct,
                    explanation = aDto.explanation
                ))
            }
            entity.quizContent = qc
        } else {
            entity.quizContent = null
        }
    }

    private fun TopicEntity.toDto() = TopicDto(
        key = key,
        name = name,
        path = path,
        parentKey = parentId?.let { topicRepository.findById(it).orElse(null)?.key },
        coverageArea = coverageArea,
        exclusions = exclusions
    )

    private fun QuestionEntity.toDto() = QuestionDto(
        key = key,
        prompt = prompt,
        difficulty = difficulty.name,
        labels = labels.toList(),
        interviewContent = interviewContent?.toDto(),
        quizContent = quizContent?.toDto()
    )

    private fun InterviewContentEntity.toDto() = InterviewContentDto(
        shortAnswer = shortAnswer,
        longAnswer = longAnswer
    )

    private fun QuizContentEntity.toDto() = QuizContentDto(
        answers = answers.map { it.toDto() }
    )

    private fun QuizAnswerEntity.toDto() = QuizAnswerDto(
        text = text,
        correct = correct,
        explanation = explanation
    )
}
