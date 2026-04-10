//package com.kdob.piq.question.infrastructure.config
//
//import com.kdob.piq.question.domain.question.Difficulty
//import com.kdob.piq.question.infrastructure.persistence.question.*
//import com.kdob.piq.question.infrastructure.persistence.topic.SpringDataTopicRepository
//import com.kdob.piq.question.infrastructure.persistence.topic.TopicEntity
//import org.slf4j.LoggerFactory
//import org.springframework.boot.CommandLineRunner
//import org.springframework.context.annotation.Profile
//import org.springframework.stereotype.Component
//import java.util.*
//
///**
// * Development initializer: seeds the database with a nested structure of Topics and Questions using real-world data.
// */
//@Component
//@Profile(value = ["default", "dev"])
//class DataInitializer(
//    private val topicRepository: SpringDataTopicRepository,
//    private val questionRepository: QuestionRepository
//) : CommandLineRunner {
//
//    private val log = LoggerFactory.getLogger(DataInitializer::class.java)
//
//    override fun run(vararg args: String) {
//        if (topicRepository.count() > 0) {
//            log.info("[DataInitializer] Database already seeded. Skipping.")
//            return
//        }
//
//        log.info("[DataInitializer] Seeding database with real technical data...")
//
//        val topLevelNames = loadTopLevelTopics()
//        val javaSubTopicNames = loadJavaSubTopics()
//
//        topLevelNames.forEach { rootName ->
//            val rootKey = rootName.lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-')
//            val rootTopic = createTopic(rootName, rootKey, null, "/$rootKey")
//
//            val hierarchyTopics = mutableListOf(rootTopic)
//
//            if (rootName == "Java") {
//                javaSubTopicNames.forEach { subName ->
//                    val subKeyPart = subName.lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-')
//                    val subKey = "${rootKey}-$subKeyPart"
//                    val subTopic = createTopic(subName, subKey, rootTopic, "/$rootKey/$subKey")
//                    hierarchyTopics.add(subTopic)
//
//                    // Real nesting levels
//                    val targetLevels = (3..7).random()
//                    var currentParent = subTopic
//                    for (level in 3..targetLevels) {
//                        val nestedName = "$subName Detail Level $level"
//                        val nestedKey = "${currentParent.key}-l$level"
//                        val nestedPath = "${currentParent.path}/$nestedKey"
//                        currentParent = createTopic(nestedName, nestedKey, currentParent, nestedPath)
//                        hierarchyTopics.add(currentParent)
//                    }
//                }
//            } else {
//                // Other topics: 10-20 subtopics, 3-6 levels
//                val totalSubtopics = (10..20).random()
//                val targetLevels = (3..6).random()
//
//                var currentParent = rootTopic
//                for (level in 2..targetLevels) {
//                    val name = "$rootName Aspects $level"
//                    val key = "${currentParent.key}-a$level"
//                    val path = "${currentParent.path}/$key"
//                    currentParent = createTopic(name, key, currentParent, path)
//                    hierarchyTopics.add(currentParent)
//                }
//
//                val remainingCount = totalSubtopics - (targetLevels - 1)
//                for (i in 1..remainingCount) {
//                    val parent = hierarchyTopics.random()
//                    val name = "$rootName Specifics $i"
//                    val key = "${parent.key}-s$i"
//                    val path = "${parent.path}/$key"
//                    val extra = createTopic(name, key, parent, path)
//                    hierarchyTopics.add(extra)
//                }
//            }
//
//            // Distribute questions: 10-40 per TLT hierarchy
//            val questionCount = (10..40).random()
//            distributeAndSeedQuestions(rootName, hierarchyTopics, questionCount)
//        }
//
//        log.info("[DataInitializer] Seeding completed.")
//    }
//
//    private fun loadTopLevelTopics(): List<String> {
//        val defaultList =
//            "APIs, Architecture, Best Practices, Building tools, Common Programming, Cloud, DataBase, Deployments, DevOps, Integrations, Interesting staff, Internet, Interview expressions, GIT, Java, Migration (Java/Spring/Spring Boot), Monitoring/Logging systems, Object-Oriented Programming, OOP (Object Oriented Programming) paradigm, Production, Security, Spring framework, System Design, Testing, My questions to AI, Virtualisation | Containerisation"
//                .split(",")
//                .map { it.trim() }
//                .filter { it.isNotEmpty() }
//
//        return try {
//            val res = javaClass.getResource("TopLevelTopics")
//                ?: javaClass.getResource("/com/kdob/piq/question/config/TopLevelTopics")
//            res?.readText()
//                ?.split(",")
//                ?.map { it.trim() }
//                ?.filter { it.isNotEmpty() }
//                ?: defaultList
//        } catch (e: Exception) {
//            log.warn("Could not load TopLevelTopics file, using defaults: ${e.message}")
//            defaultList
//        }
//    }
//
//    private fun loadJavaSubTopics(): List<String> {
//        val defaultList = "Concurrency, Basics & Language Components, Collections, Exceptions, JVM & Memory Management"
//            .split(",")
//            .map { it.trim() }
//            .filter { it.isNotEmpty() }
//
//        return try {
//            val res = javaClass.getResource("JavaSubTopics")
//                ?: javaClass.getResource("/com/kdob/piq/question/config/JavaSubTopics")
//            res?.readText()
//                ?.split(",")
//                ?.map { it.trim() }
//                ?.filter { it.isNotEmpty() }
//                ?: defaultList
//        } catch (e: Exception) {
//            log.warn("Could not load JavaSubTopics file, using defaults: ${e.message}")
//            defaultList
//        }
//    }
//
//    private fun distributeAndSeedQuestions(rootName: String, topics: List<TopicEntity>, totalQuestions: Int) {
//        val questionsPerTopic = totalQuestions / topics.size
//        val remainder = totalQuestions % topics.size
//
//        var questionCounter = 0
//        topics.forEachIndexed { index, topic ->
//            val countForThisTopic = questionsPerTopic + (if (index < remainder) 1 else 0)
//            if (countForThisTopic > 0) {
//                seedQuestionsForTopic(rootName, topic, countForThisTopic, questionCounter)
//                questionCounter += countForThisTopic
//            }
//        }
//    }
//
//    private fun seedQuestionsForTopic(rootName: String, topic: TopicEntity, count: Int, offset: Int) {
//        val questions = mutableListOf<QuestionEntity>()
//        val realContent = getRealContentForTopic(rootName, topic.name)
//
//        for (i in 1..count) {
//            val contentIdx = (offset + i - 1) % realContent.size
//            val realQ = realContent[contentIdx]
//            val typeIndex = (offset + i - 1) % 3
//
//            val question = QuestionEntity(
//                key = "${topic.key}-q-${UUID.randomUUID().toString().take(8)}",
//                prompt = realQ.prompt,
//                difficulty = realQ.difficulty,
//                topic = topic,
//                labels = setOf("seeded", rootName.lowercase())
//            )
//
//            // Distribution: 1/3 Interview only, 1/3 Quiz only, 1/3 Both
//            if (typeIndex == 0 || typeIndex == 2) {
//                // Interview content
//                question.interviewContent = InterviewContentEntity(
//                    question = question,
//                    shortAnswer = realQ.shortAnswer,
//                    longAnswer = realQ.longAnswer
//                )
//            }
//
//            if (typeIndex == 1 || typeIndex == 2) {
//                // Quiz content
//                val answersList = mutableListOf<QuizAnswerEntity>()
//                val quizContent = QuizContentEntity(
//                    question = question,
//                    answers = answersList
//                )
//                realQ.quizOptions.forEach { opt ->
//                    answersList.add(
//                        QuizAnswerEntity(
//                            quizContent = quizContent,
//                            text = opt.text,
//                            correct = opt.correct,
//                            explanation = opt.explanation
//                        )
//                    )
//                }
//                question.quizContent = quizContent
//            }
//            questions.add(question)
//        }
//        questionRepository.saveAll(questions)
//    }
//
//    private fun createTopic(name: String, key: String, parent: TopicEntity?, path: String): TopicEntity {
//        val topic = TopicEntity(
//            key = key,
//            name = name,
//            parentId = parent?.id,
//            path = path
//        )
//        val saved = topicRepository.save(topic)
//        log.debug("[DataInitializer] Created topic: {}", saved.path)
//        return saved
//    }
//
//    // --- REAL DATA CONTENT ---
//
//    private fun getRealContentForTopic(root: String, current: String): List<RealQuestionData> {
//        return when (root) {
//            "Java" -> getJavaQuestions()
//            "Spring framework" -> getSpringQuestions()
//            "Architecture" -> getArchitectureQuestions()
//            "DataBase" -> getDatabaseQuestions()
//            "DevOps" -> getDevOpsQuestions()
//            "Testing" -> getTestingQuestions()
//            "Security" -> getSecurityQuestions()
//            else -> getGeneralITQuestions(current)
//        }
//    }
//
//    private data class RealQuestionData(
//        val prompt: String,
//        val difficulty: Difficulty,
//        val shortAnswer: String,
//        val longAnswer: String,
//        val quizOptions: List<RealQuizOption>
//    )
//
//    private data class RealQuizOption(
//        val text: String,
//        val correct: Boolean,
//        val explanation: String
//    )
//
//    private fun getJavaQuestions(): List<RealQuestionData> {
//        return listOf(
//            RealQuestionData(
//                "What is the difference between `final`, `finally`, and `finalize`?",
//                Difficulty.EASY,
//                "`final` is a keyword to restrict change, `finally` is used in exception handling for cleanup, and `finalize` is a method for object cleanup (deprecated).",
//                """
//                ### Java Keywords Comparison
//                - **final**: Used to define constants, prevent method overriding, or prevent class inheritance.
//                - **finally**: A block that always executes after try-catch, regardless of whether an exception was thrown. Used for resource cleanup.
//                - **finalize**: A method called by the Garbage Collector before an object is reclaimed. It is deprecated since Java 9.
//                """.trimIndent(),
//                listOf(
//                    RealQuizOption(
//                        "final is for inheritance, finally is for cleanup, finalize is for GC",
//                        true,
//                        "This correctly identifies the primary purpose of each term."
//                    ),
//                    RealQuizOption(
//                        "They are all for exception handling",
//                        false,
//                        "Only finally is primarily for exception handling."
//                    ),
//                    RealQuizOption(
//                        "final can only be applied to variables",
//                        false,
//                        "final can be applied to classes and methods too."
//                    )
//                )
//            ),
//            RealQuestionData(
//                "Explain the Java Memory Model (Heap vs Stack).",
//                Difficulty.MEDIUM,
//                "Heap is used for dynamic memory allocation of objects, while Stack is used for thread-specific execution and local variables.",
//                """
//                ### Java Memory Areas
//                1. **Heap**:
//                   - Shared across all threads.
//                   - Stores objects and JRE classes.
//                   - Managed by Garbage Collector.
//                2. **Stack**:
//                   - Private to each thread.
//                   - Stores local variables and method call frames.
//                   - LIFO structure, automatically cleaned up when method returns.
//                """.trimIndent(),
//                listOf(
//                    RealQuizOption(
//                        "Heap stores objects, Stack stores local variables",
//                        true,
//                        "This is the fundamental distinction in JVM memory management."
//                    ),
//                    RealQuizOption("Stack is shared between all threads", false, "Stack is thread-private."),
//                    RealQuizOption(
//                        "Objects are stored on the Stack for performance",
//                        false,
//                        "Objects are almost always on the Heap (except for some escape analysis optimizations)."
//                    )
//                )
//            ),
//            RealQuestionData(
//                "What is a `FunctionalInterface` in Java?",
//                Difficulty.MEDIUM,
//                "An interface with exactly one abstract method, used as the basis for Lambda expressions.",
//                """
//                ### Functional Interfaces in Java 8+
//                - Can have any number of default or static methods.
//                - Must have exactly **one** abstract method.
//                - Annotated with `@FunctionalInterface` (optional but recommended).
//                - Examples: `Predicate`, `Consumer`, `Function`, `Supplier`.
//                """.trimIndent(),
//                listOf(
//                    RealQuizOption(
//                        "An interface with only one abstract method",
//                        true,
//                        "This is the definition of a functional interface."
//                    ),
//                    RealQuizOption("An interface with no methods", false, "That would be a marker interface."),
//                    RealQuizOption(
//                        "An interface used for serializing objects",
//                        false,
//                        "That is the Serializable interface."
//                    )
//                )
//            ),
//            RealQuestionData(
//                "What is the difference between `ArrayList` and `LinkedList`?",
//                Difficulty.EASY,
//                "`ArrayList` uses a dynamic array for storage, while `LinkedList` uses a doubly linked list.",
//                """
//                ### ArrayList vs LinkedList
//                - **ArrayList**:
//                  - Fast random access (O(1)).
//                  - Slow insertion/deletion in the middle (O(n)).
//                  - Better for search-intensive applications.
//                - **LinkedList**:
//                  - Slow random access (O(n)).
//                  - Fast insertion/deletion at both ends (O(1)).
//                  - Better for frequent modifications.
//                """.trimIndent(),
//                listOf(
//                    RealQuizOption(
//                        "ArrayList is better for random access",
//                        true,
//                        "Because it uses an underlying array."
//                    ),
//                    RealQuizOption(
//                        "LinkedList uses less memory",
//                        false,
//                        "Actually LinkedList uses more memory because each node stores references to previous and next elements."
//                    ),
//                    RealQuizOption(
//                        "ArrayList is synchronized",
//                        false,
//                        "Neither is synchronized by default; use Vector or Collections.synchronizedList if needed."
//                    )
//                )
//            ),
//            RealQuestionData(
//                "What is the `Optional` class used for in Java 8?",
//                Difficulty.MEDIUM,
//                "A container object which may or may not contain a non-null value, used to avoid `NullPointerException`.",
//                """
//                ### Java Optional
//                - Encourages cleaner code by explicitly representing the absence of a value.
//                - Methods like `orElse()`, `ifPresent()`, and `map()` provide a functional way to handle optional values.
//                - **Best Practice**: Use it for return types of methods that might not have a result.
//                """.trimIndent(),
//                listOf(
//                    RealQuizOption(
//                        "To represent a value that may be missing",
//                        true,
//                        "This is the primary purpose of Optional."
//                    ),
//                    RealQuizOption(
//                        "To make code run faster",
//                        false,
//                        "It actually adds a small overhead, but improves code quality."
//                    ),
//                    RealQuizOption(
//                        "To replace all null checks in the system",
//                        false,
//                        "It's best used for return values, not for all fields or parameters."
//                    )
//                )
//            )
//        )
//    }
//
//    private fun getSpringQuestions(): List<RealQuestionData> {
//        return listOf(
//            RealQuestionData(
//                "What is Dependency Injection (DI) in Spring?",
//                Difficulty.EASY,
//                "DI is a design pattern where the container provides the dependencies of a class rather than the class creating them itself.",
//                """
//                ### Dependency Injection Benefits
//                - **Loose Coupling**: Components are not responsible for instantiating their dependencies.
//                - **Testability**: Dependencies can be easily mocked during unit testing.
//                - **Maintainability**: Configuration of dependencies is centralized in the Spring container.
//
//                Spring supports DI through **Constructor**, **Setter**, and **Field** injection.
//                """.trimIndent(),
//                listOf(
//                    RealQuizOption(
//                        "The container provides dependencies to objects",
//                        true,
//                        "This is the core concept of DI."
//                    ),
//                    RealQuizOption(
//                        "It's a way to inject SQL into a database",
//                        false,
//                        "That would be SQL injection, which is a security vulnerability."
//                    ),
//                    RealQuizOption(
//                        "Objects create their own dependencies using 'new'",
//                        false,
//                        "That's the opposite of DI."
//                    )
//                )
//            ),
//            RealQuestionData(
//                "Explain `@SpringBootApplication` annotation.",
//                Difficulty.MEDIUM,
//                "A convenience annotation that combines `@Configuration`, `@EnableAutoConfiguration`, and `@ComponentScan`.",
//                """
//                ### Spring Boot Entry Point
//                - **@Configuration**: Tags the class as a source of bean definitions.
//                - **@EnableAutoConfiguration**: Tells Spring Boot to start adding beans based on classpath settings.
//                - **@ComponentScan**: Tells Spring to look for other components, configurations, and services in the package.
//                """.trimIndent(),
//                listOf(
//                    RealQuizOption(
//                        "Combines Configuration, EnableAutoConfiguration, and ComponentScan",
//                        true,
//                        "This is exactly what the source code of @SpringBootApplication shows."
//                    ),
//                    RealQuizOption(
//                        "Used only for database configuration",
//                        false,
//                        "It is used for the whole application bootstrap."
//                    ),
//                    RealQuizOption(
//                        "Enables only security features",
//                        false,
//                        "Security is just one part of auto-configuration."
//                    )
//                )
//            ),
//            RealQuestionData(
//                "What is the difference between `@Component`, `@Service`, and `@Repository`?",
//                Difficulty.MEDIUM,
//                "These are all stereotypes for Spring-managed components; `@Service` and `@Repository` are specialized versions of `@Component`.",
//                """
//                ### Spring Stereotypes
//                - **@Component**: General-purpose bean.
//                - **@Service**: Used for business logic layer.
//                - **@Repository**: Used for data access layer; it also enables automatic exception translation.
//                - **@Controller**: Used for the presentation layer (Spring MVC).
//                """.trimIndent(),
//                listOf(
//                    RealQuizOption(
//                        "Service and Repository are specialized Components",
//                        true,
//                        "This is correct; they are meta-annotated with @Component."
//                    ),
//                    RealQuizOption(
//                        "Repository is used for creating SQL tables",
//                        false,
//                        "Repository is for data access, not schema creation."
//                    ),
//                    RealQuizOption(
//                        "They are used in different Spring versions",
//                        false,
//                        "They are all available in modern Spring."
//                    )
//                )
//            )
//        )
//    }
//
//    private fun getArchitectureQuestions(): List<RealQuestionData> {
//        return listOf(
//            RealQuestionData(
//                "What are the SOLID principles?",
//                Difficulty.HARD,
//                "Five design principles for writing maintainable and scalable software: SRP, OCP, LSP, ISP, and DIP.",
//                """
//                ### SOLID Principles
//                1. **S**RP: Single Responsibility Principle.
//                2. **O**CP: Open/Closed Principle.
//                3. **L**SP: Liskov Substitution Principle.
//                4. **I**SP: Interface Segregation Principle.
//                5. **D**IP: Dependency Inversion Principle.
//                """.trimIndent(),
//                listOf(
//                    RealQuizOption("SRP, OCP, LSP, ISP, DIP", true, "These are the five principles of SOLID."),
//                    RealQuizOption("Standard, Object, Logic, Interface, Data", false, "Incorrect names."),
//                    RealQuizOption(
//                        "Security, Optimization, Logging, Integration, Deployment",
//                        false,
//                        "These are cross-cutting concerns, not SOLID."
//                    )
//                )
//            ),
//            RealQuestionData(
//                "What is Microservices architecture?",
//                Difficulty.MEDIUM,
//                "An architectural style that structures an application as a collection of small, loosely coupled services.",
//                """
//                ### Microservices Key Characteristics
//                - **Independently Deployable**: Each service can be updated without redeploying the whole system.
//                - **Technological Freedom**: Services can use different languages or databases.
//                - **Scalability**: Individual services can be scaled based on their specific needs.
//                - **Fault Isolation**: Failure of one service doesn't necessarily take down the whole system.
//                """.trimIndent(),
//                listOf(
//                    RealQuizOption(
//                        "A collection of small, loosely coupled services",
//                        true,
//                        "This is the standard definition."
//                    ),
//                    RealQuizOption(
//                        "One large database for all application logic",
//                        false,
//                        "This describes a monolithic database, often discouraged in microservices."
//                    ),
//                    RealQuizOption(
//                        "Running multiple instances of a monolith",
//                        false,
//                        "That is just horizontal scaling of a monolith."
//                    )
//                )
//            )
//        )
//    }
//
//    private fun getDatabaseQuestions(): List<RealQuestionData> {
//        return listOf(
//            RealQuestionData(
//                "What is ACID in database transactions?",
//                Difficulty.MEDIUM,
//                "A set of properties (Atomicity, Consistency, Isolation, Durability) that guarantee database transactions are processed reliably.",
//                """
//                ### ACID Properties
//                - **Atomicity**: 'All or nothing' - either the whole transaction succeeds or none of it is applied.
//                - **Consistency**: The transaction brings the database from one valid state to another.
//                - **Isolation**: Concurrent transactions don't interfere with each other.
//                - **Durability**: Once a transaction is committed, it remains committed even in case of power failure.
//                """.trimIndent(),
//                listOf(
//                    RealQuizOption("Atomicity, Consistency, Isolation, Durability", true, "Correct acronym meaning."),
//                    RealQuizOption(
//                        "Availability, Consistency, Isolation, Durability",
//                        false,
//                        "Availability is part of CAP theorem, not ACID."
//                    ),
//                    RealQuizOption("Access, Control, Information, Data", false, "Incorrect terms.")
//                )
//            )
//        )
//    }
//
//    private fun getDevOpsQuestions(): List<RealQuestionData> {
//        return listOf(
//            RealQuestionData(
//                "What is CI/CD?",
//                Difficulty.EASY,
//                "Continuous Integration and Continuous Deployment/Delivery, focusing on automating the software release lifecycle.",
//                """
//                ### CI/CD Pipeline
//                - **CI (Continuous Integration)**: Automating the build and testing of code every time a team member pushes changes to version control.
//                - **CD (Continuous Delivery)**: Ensuring the software is always in a state where it can be deployed to production.
//                - **CD (Continuous Deployment)**: Automating the actual release to production.
//                """.trimIndent(),
//                listOf(
//                    RealQuizOption("Continuous Integration and Continuous Delivery", true, "Correct definition."),
//                    RealQuizOption("Code Inspection and Code Debugging", false, "Incorrect."),
//                    RealQuizOption(
//                        "Cloud Integration and Cloud Development",
//                        false,
//                        "Though related to cloud, that's not what CI/CD stands for."
//                    )
//                )
//            )
//        )
//    }
//
//    private fun getTestingQuestions(): List<RealQuestionData> {
//        return listOf(
//            RealQuestionData(
//                "What is the difference between Unit and Integration tests?",
//                Difficulty.EASY,
//                "Unit tests check individual components in isolation; Integration tests check how multiple components work together.",
//                """
//                ### Testing Types
//                - **Unit Tests**: Test a single class or method. Fast, uses mocks for dependencies.
//                - **Integration Tests**: Test the interaction between modules, databases, or external services. Slower, more complex setup.
//                """.trimIndent(),
//                listOf(
//                    RealQuizOption("Unit tests isolated, Integration tests combined", true, "Correct distinction."),
//                    RealQuizOption("Unit tests are for UI only", false, "Unit tests are typically for business logic."),
//                    RealQuizOption(
//                        "There is no difference",
//                        false,
//                        "They serve very different purposes in the test pyramid."
//                    )
//                )
//            )
//        )
//    }
//
//    private fun getSecurityQuestions(): List<RealQuestionData> {
//        return listOf(
//            RealQuestionData(
//                "What is OAuth2?",
//                Difficulty.HARD,
//                "An open standard for access delegation, commonly used as a way for Internet users to grant websites access to their information on other websites.",
//                """
//                ### OAuth2 Roles
//                - **Resource Owner**: The user.
//                - **Client**: The application requesting access.
//                - **Authorization Server**: The server issuing tokens.
//                - **Resource Server**: The server hosting the protected data.
//                """.trimIndent(),
//                listOf(
//                    RealQuizOption(
//                        "A framework for authorization",
//                        true,
//                        "OAuth2 is specifically for authorization (access delegation)."
//                    ),
//                    RealQuizOption(
//                        "A framework for authentication only",
//                        false,
//                        "SAML or OIDC are more about authentication; OAuth2 is authorization."
//                    ),
//                    RealQuizOption("A database encryption tool", false, "Incorrect.")
//                )
//            )
//        )
//    }
//
//    private fun getGeneralITQuestions(topicName: String): List<RealQuestionData> {
//        return listOf(
//            RealQuestionData(
//                "What is the importance of $topicName in modern development?",
//                Difficulty.MEDIUM,
//                "Understanding $topicName is crucial for building robust, scalable, and secure systems.",
//                """
//                ### $topicName in Depth
//                - **Relevance**: $topicName affects performance and maintainability.
//                - **Best Practice**: Always follow industry standards for $topicName.
//                - **Evolution**: Modern tools have simplified $topicName, but core concepts remain essential.
//                """.trimIndent(),
//                listOf(
//                    RealQuizOption("It improves system quality", true, "General positive impact."),
//                    RealQuizOption("It is optional and rarely used", false, "Most topics in this list are essential."),
//                    RealQuizOption(
//                        "It only matters for legacy systems",
//                        false,
//                        "Contemporary development relies on these concepts."
//                    )
//                )
//            )
//        )
//    }
//}
