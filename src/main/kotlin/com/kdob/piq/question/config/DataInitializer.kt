package com.kdob.piq.question.config

import com.kdob.piq.question.api.dto.CreateLearningItemRequest
import com.kdob.piq.question.api.dto.topic.CreateTopicRequest
import com.kdob.piq.question.application.AdminLearningItemService
import com.kdob.piq.question.application.AdminTopicService
import com.kdob.piq.question.domain.answer.Answer
import com.kdob.piq.question.domain.presentation.Presentation
import com.kdob.piq.question.domain.presentation.PresentationFormat
import com.kdob.piq.question.persistence.answer.JpaAnswerCommandRepository
import com.kdob.piq.question.persistence.presentation.JpaPresentationCommandRepository
import com.kdob.piq.question.persistence.topic.JpaTopicQueryRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Profile("default", "dev")
class DataInitializer(
    private val topicQueryRepository: JpaTopicQueryRepository,
    private val adminTopicService: AdminTopicService,
    private val adminLearningItemService: AdminLearningItemService,
    private val presentationRepo: JpaPresentationCommandRepository,
    private val answerRepo: JpaAnswerCommandRepository
) : CommandLineRunner {
    private val log = LoggerFactory.getLogger(DataInitializer::class.java)

    @Transactional
    override fun run(vararg args: String) {
        if (topicQueryRepository.findAll().isNotEmpty()) {
            log.info("[DataInitializer] Data already exists, skipping initialization.")
            return
        }

        log.info("[DataInitializer] Initializing test data...")

        val root = TopicNode("it", "Information Technology", listOf(
            TopicNode("software-dev", "Software Development", listOf(
                TopicNode("backend", "Backend Development", listOf(
                    TopicNode("backend-languages", "Programming Languages", listOf(
                        TopicNode("jvm", "JVM Ecosystem", listOf(
                            TopicNode("java", "Java", listOf(
                                TopicNode("java-core", "Java Core", listOf(
                                    TopicNode("collections", "Collections Framework", listOf(
                                        TopicNode("list", "Lists", listOf(
                                            TopicNode("array-list", "ArrayList", emptyList(), javaCollectionsQuestions)
                                        ))
                                    )),
                                    TopicNode("concurrency", "Concurrency", emptyList(), javaConcurrencyQuestions)
                                ))
                            )),
                            TopicNode("kotlin", "Kotlin", listOf(
                                TopicNode("kotlin-core", "Kotlin Core", listOf(
                                    TopicNode("null-safety", "Null Safety", emptyList(), kotlinNullSafetyQuestions)
                                ))
                            ))
                        )),
                        TopicNode("sql", "Structured Query Language", listOf(
                            TopicNode("sql-core", "SQL Core", listOf(
                                TopicNode("queries", "SQL Queries", emptyList(), sqlQuestions)
                            ))
                        ))
                    )),
                    TopicNode("backend-frameworks", "Backend Frameworks", listOf(
                        TopicNode("spring", "Spring Framework", listOf(
                            TopicNode("spring-boot", "Spring Boot", listOf(
                                TopicNode("sb-core", "Spring Boot Core", listOf(
                                    TopicNode("annotations", "Spring Boot Annotations", emptyList(), springBootQuestions)
                                ))
                            ))
                        ))
                    ))
                )),
                TopicNode("frontend", "Frontend Development", listOf(
                    TopicNode("web", "Web Technologies", listOf(
                        TopicNode("frontend-frameworks", "Frameworks & Libraries", listOf(
                            TopicNode("react", "React", listOf(
                                TopicNode("react-core", "React Core", listOf(
                                    TopicNode("hooks", "React Hooks", emptyList(), reactHooksQuestions)
                                ))
                            )),
                            TopicNode("nextjs", "Next.js", listOf(
                                TopicNode("nextjs-core", "Next.js Core", listOf(
                                    TopicNode("routing", "Next.js Routing", emptyList(), nextjsQuestions)
                                ))
                            ))
                        )),
                        TopicNode("frontend-languages", "Languages", listOf(
                            TopicNode("typescript", "TypeScript", listOf(
                                TopicNode("ts-core", "TypeScript Core", listOf(
                                    TopicNode("advanced-types", "Advanced Types", emptyList(), typescriptQuestions)
                                ))
                            ))
                        ))
                    ))
                )),
                TopicNode("architecture", "System Architecture", listOf(
                    TopicNode("cloud-native", "Cloud Native", listOf(
                        TopicNode("microservices", "Microservices", listOf(
                            TopicNode("patterns", "Design Patterns", listOf(
                                TopicNode("ms-patterns", "Microservices Patterns", listOf(
                                    TopicNode("dist-tx", "Distributed Transactions", emptyList(), microservicesQuestions)
                                ))
                            ))
                        ))
                    ))
                ))
            ))
        ))

        createTree(root, null)
        log.info("[DataInitializer] Test data initialization complete.")
    }

    private fun createTree(node: TopicNode, parentPath: String?) {
        val currentPath = if (parentPath == null) "/${node.key}" else "$parentPath/${node.key}"
        adminTopicService.createTopic(CreateTopicRequest(node.key, node.name, parentPath))

        node.questions.forEach { q ->
            val item = adminLearningItemService.createItem(CreateLearningItemRequest(listOf(currentPath)))
            val presentation = presentationRepo.save(Presentation(
                id = null,
                learningItemId = item.id!!,
                format = PresentationFormat.QUIZ,
                prompt = q.prompt,
                answers = emptyList()
            ))

            q.answers.forEach { a ->
                answerRepo.save(Answer(
                    id = null,
                    text = a.text,
                    correct = a.correct,
                    explanation = a.explanation
                ), presentation.id!!)
            }
        }

        node.children.forEach { createTree(it, currentPath) }
    }

    data class TopicNode(
        val key: String,
        val name: String,
        val children: List<TopicNode> = emptyList(),
        val questions: List<QuestionNode> = emptyList()
    )

    data class QuestionNode(
        val prompt: String,
        val answers: List<AnswerNode>
    )

    data class AnswerNode(
        val text: String,
        val correct: Boolean,
        val explanation: String? = null
    )

    private val javaCollectionsQuestions = listOf(
        QuestionNode("What is the difference between ArrayList and LinkedList?", listOf(
            AnswerNode("ArrayList is based on a dynamic array, while LinkedList is based on a doubly linked list.", true),
            AnswerNode("ArrayList provides O(1) time for positional access, while LinkedList provides O(n).", true),
            AnswerNode("LinkedList is faster for frequent additions/removals in the middle of the list than ArrayList.", true),
            AnswerNode("ArrayList is generally more memory-efficient as it only stores the elements and not the node pointers.", true),
            AnswerNode("Both are synchronized and thread-safe by default.", false, "They are not synchronized; use Collections.synchronizedList if needed.")
        )),
        QuestionNode("How does HashMap work internally in Java 8?", listOf(
            AnswerNode("It uses an array of buckets, where each bucket is a linked list or a balanced tree.", true),
            AnswerNode("When a bucket size exceeds a threshold (8), the linked list is converted into a red-black tree.", true),
            AnswerNode("It uses the hashCode() and equals() methods to store and retrieve elements.", true),
            AnswerNode("The default load factor is 0.75.", true),
            AnswerNode("It maintains the insertion order of elements.", false, "LinkedHashMap maintains insertion order, not HashMap.")
        )),
        QuestionNode("What is the difference between fail-fast and fail-safe iterators?", listOf(
            AnswerNode("Fail-fast iterators throw ConcurrentModificationException if the collection is modified during iteration.", true),
            AnswerNode("Fail-safe iterators operate on a clone of the collection and do not throw exception.", true),
            AnswerNode("ArrayList iterator is fail-fast.", true),
            AnswerNode("CopyOnWriteArrayList iterator is fail-safe.", true),
            AnswerNode("Fail-safe iterators are always slower and should be avoided.", false)
        )),
        QuestionNode("Why is the Map interface not a subtype of Collection in Java?", listOf(
            AnswerNode("Map is key-value based, while Collection is element-based.", true),
            AnswerNode("The add(E e) method of Collection doesn't make sense for Map, which needs a key and a value.", true),
            AnswerNode("Their design philosophies are different; Map represents a mapping, not a group of objects.", true),
            AnswerNode("Technically, Map could have been a Collection of Map.Entry, but it was decided to keep them separate for clarity.", true),
            AnswerNode("Maps cannot be iterated directly without using entrySet(), keySet() or values().", true)
        )),
        QuestionNode("What is the purpose of ConcurrentHashMap in Java?", listOf(
            AnswerNode("To provide a thread-safe map with better performance than Hashtable or SynchronizedMap.", true),
            AnswerNode("It allows multiple threads to read and write concurrently without locking the entire map.", true),
            AnswerNode("It uses bucket-level locking (or CAS operations in recent versions) instead of global locking.", true),
            AnswerNode("It does not allow null keys or null values.", true),
            AnswerNode("It guarantees that reads will always reflect the most recently completed write.", true)
        ))
    )

    private val javaConcurrencyQuestions = listOf(
        QuestionNode("What is the difference between wait() and sleep() in Java?", listOf(
            AnswerNode("wait() is a method of Object class, while sleep() is a static method of Thread class.", true),
            AnswerNode("wait() releases the lock, while sleep() does not release the lock.", true),
            AnswerNode("wait() must be called from a synchronized context, while sleep() does not.", true),
            AnswerNode("wait() is used for inter-thread communication, while sleep() is used to pause execution.", true),
            AnswerNode("Both methods throw InterruptedException.", true)
        )),
        QuestionNode("What is the purpose of the 'volatile' keyword in Java?", listOf(
            AnswerNode("It ensures that a variable is always read from and written to the main memory.", true),
            AnswerNode("It prevents instruction reordering by the compiler and JVM for that variable.", true),
            AnswerNode("It provides visibility of changes to other threads.", true),
            AnswerNode("It does NOT provide atomicity (e.g., for incrementing a variable).", true),
            AnswerNode("It can be used as a replacement for 'synchronized' in all cases.", false)
        )),
        QuestionNode("How does ThreadLocal work in Java?", listOf(
            AnswerNode("It provides thread-local variables that are independent for each thread.", true),
            AnswerNode("Each thread holds an implicit reference to its copy of a thread-local variable.", true),
            AnswerNode("It's commonly used to store security context or database transactions.", true),
            AnswerNode("It can cause memory leaks if not properly removed in thread-pool environments.", true),
            AnswerNode("It's a way to share data between different threads.", false)
        )),
        QuestionNode("What is the difference between execute() and submit() in ExecutorService?", listOf(
            AnswerNode("execute() is defined in Executor interface, while submit() is in ExecutorService.", true),
            AnswerNode("execute() returns void, while submit() returns a Future object.", true),
            AnswerNode("submit() can take both Runnable and Callable tasks, while execute() only takes Runnable.", true),
            AnswerNode("Exceptions in execute() are handled by the UncaughtExceptionHandler, while in submit() they are captured in Future.", true),
            AnswerNode("There is no difference in how they execute tasks.", false)
        )),
        QuestionNode("What is a Deadlock and how can it be prevented?", listOf(
            AnswerNode("A deadlock occurs when two or more threads are blocked forever, each waiting for the other.", true),
            AnswerNode("It can be prevented by acquiring locks in a consistent global order.", true),
            AnswerNode("Using tryLock() with a timeout can help prevent deadlocks.", true),
            AnswerNode("Avoiding nested locks can reduce the risk of deadlocks.", true),
            AnswerNode("Deadlocks are automatically resolved by the JVM after a few seconds.", false)
        ))
    )

    private val springBootQuestions = listOf(
        QuestionNode("What is the purpose of @SpringBootApplication annotation?", listOf(
            AnswerNode("It's a convenience annotation that combines @Configuration, @EnableAutoConfiguration, and @ComponentScan.", true),
            AnswerNode("It marks the main class of a Spring Boot application.", true),
            AnswerNode("It enables component scanning in the package where the class is located.", true),
            AnswerNode("It enables Spring Boot's auto-configuration mechanism.", true),
            AnswerNode("It replaces the need for an external web server.", false)
        )),
        QuestionNode("How can you change the default port of a Spring Boot application?", listOf(
            AnswerNode("By setting server.port in application.properties or application.yml.", true),
            AnswerNode("By passing --server.port=8081 as a command-line argument.", true),
            AnswerNode("By setting the SERVER_PORT environment variable.", true),
            AnswerNode("By using a custom WebServerFactoryCustomizer bean.", true),
            AnswerNode("By changing the port in the pom.xml file.", false)
        )),
        QuestionNode("What is Spring Boot Starter?", listOf(
            AnswerNode("A set of convenient dependency descriptors you can include in your application.", true),
            AnswerNode("It simplifies the Maven/Gradle configuration by providing a curated set of dependencies.", true),
            AnswerNode("Examples include spring-boot-starter-web and spring-boot-starter-data-jpa.", true),
            AnswerNode("It automatically handles versioning for compatible libraries.", true),
            AnswerNode("It's a GUI tool to start new Spring projects.", false)
        )),
        QuestionNode("Explain the difference between @RestController and @Controller.", listOf(
            AnswerNode("@RestController is a specialized version of @Controller.", true),
            AnswerNode("@RestController is a combination of @Controller and @ResponseBody.", true),
            AnswerNode("Methods in @RestController return data directly (JSON/XML) instead of a view name.", true),
            AnswerNode("@Controller is typically used for traditional MVC applications with JSP/Thymeleaf.", true),
            AnswerNode("@RestController cannot be used with @RequestMapping.", false)
        )),
        QuestionNode("What is the purpose of Spring Boot Actuator?", listOf(
            AnswerNode("To provide production-ready features like health checks and metrics.", true),
            AnswerNode("It allows monitoring and managing the application via HTTP or JMX endpoints.", true),
            AnswerNode("Endpoints like /health, /metrics, and /env are provided by default.", true),
            AnswerNode("It helps in troubleshooting by providing insights into the running application.", true),
            AnswerNode("It is used to activate Spring profiles at runtime.", false)
        ))
    )

    private val sqlQuestions = listOf(
        QuestionNode("What is the difference between INNER JOIN and LEFT JOIN?", listOf(
            AnswerNode("INNER JOIN returns only matching rows from both tables.", true),
            AnswerNode("LEFT JOIN returns all rows from the left table and matching rows from the right table.", true),
            AnswerNode("If there's no match, LEFT JOIN returns NULL for the right table's columns.", true),
            AnswerNode("INNER JOIN can result in a smaller result set than LEFT JOIN.", true),
            AnswerNode("They always return the same number of rows if all foreign keys are valid.", false)
        )),
        QuestionNode("What are Database Indexes and why are they used?", listOf(
            AnswerNode("They are data structures that improve the speed of data retrieval operations.", true),
            AnswerNode("They work similarly to an index in a book, allowing quick lookups.", true),
            AnswerNode("Indexes can be created on one or more columns.", true),
            AnswerNode("While they speed up reads, they can slow down write operations (INSERT, UPDATE).", true),
            AnswerNode("They are used to encrypt sensitive data in the database.", false)
        )),
        QuestionNode("Explain the difference between WHERE and HAVING clauses.", listOf(
            AnswerNode("WHERE is used to filter rows before grouping.", true),
            AnswerNode("HAVING is used to filter groups after the GROUP BY clause has been applied.", true),
            AnswerNode("Aggregate functions (SUM, AVG) can be used in HAVING but not typically in WHERE.", true),
            AnswerNode("WHERE can be used without GROUP BY, but HAVING is usually used with it.", true),
            AnswerNode("There is no difference; they are interchangeable.", false)
        )),
        QuestionNode("What are ACID properties in a database?", listOf(
            AnswerNode("Atomicity, Consistency, Isolation, Durability.", true),
            AnswerNode("Atomicity: All operations in a transaction succeed or none do.", true),
            AnswerNode("Consistency: A transaction takes the database from one valid state to another.", true),
            AnswerNode("Isolation: Concurrent transactions do not interfere with each other.", true),
            AnswerNode("Durability: Once a transaction is committed, it remains even after a system failure.", true)
        )),
        QuestionNode("What is a Primary Key?", listOf(
            AnswerNode("A column or group of columns that uniquely identifies each row in a table.", true),
            AnswerNode("It must contain unique values and cannot contain NULLs.", true),
            AnswerNode("A table can have only one primary key.", true),
            AnswerNode("It is often used as a target for foreign keys in other tables.", true),
            AnswerNode("It can be changed frequently without any impact on data integrity.", false)
        ))
    )

    private val kotlinNullSafetyQuestions = listOf(
        QuestionNode("What is the Elvis operator (?:) in Kotlin?", listOf(
            AnswerNode("It returns the left-hand operand if it is not null, otherwise it returns the right-hand operand.", true),
            AnswerNode("It can be used to provide a default value for nullable types.", true),
            AnswerNode("It can also be used to return or throw from a function if a value is null.", true),
            AnswerNode("It's called the Elvis operator because of its resemblance to Elvis Presley's hair.", true),
            AnswerNode("It is a ternary operator like in Java.", false)
        )),
        QuestionNode("Explain the difference between ?. and !! operators in Kotlin.", listOf(
            AnswerNode("?. is the safe call operator; it returns null if the receiver is null.", true),
            AnswerNode("!! is the not-null assertion operator; it throws NullPointerException if the receiver is null.", true),
            AnswerNode("Safe call can be chained: person?.address?.city.", true),
            AnswerNode("Use !! only when you are 100% sure the value is not null.", true),
            AnswerNode("!! is generally preferred over ?. for better performance.", false)
        )),
        QuestionNode("How do you handle nullability when interacting with Java code in Kotlin?", listOf(
            AnswerNode("Types coming from Java are seen as 'platform types' (T!), which means nullability is unknown.", true),
            AnswerNode("Kotlin compiler doesn't enforce null-safety for platform types, but it can crash at runtime.", true),
            AnswerNode("Using @Nullable or @NotNull annotations in Java helps Kotlin determine nullability.", true),
            AnswerNode("You should explicitly specify the type as nullable or non-nullable when receiving from Java.", true),
            AnswerNode("Kotlin converts all Java types to non-nullable types by default.", false)
        )),
        QuestionNode("What is a 'Safe Cast' operator (as?) in Kotlin?", listOf(
            AnswerNode("It tries to cast an object to a type and returns null if the cast fails.", true),
            AnswerNode("It avoids ClassCastException at runtime.", true),
            AnswerNode("It's often used with the Elvis operator: val x = y as? Int ?: 0.", true),
            AnswerNode("The regular 'as' operator throws an exception if the cast is impossible.", true),
            AnswerNode("Safe cast is only for primitive types.", false)
        )),
        QuestionNode("What is the difference between lateinit and lazy in Kotlin?", listOf(
            AnswerNode("lateinit is for var, while lazy is for val.", true),
            AnswerNode("lateinit does not support primitive types like Int or Boolean.", true),
            AnswerNode("lazy properties are initialized only upon first access.", true),
            AnswerNode("lateinit means 'I will initialize this before use', while lazy is 'I don't know when I'll need this'.", true),
            AnswerNode("Both can be used for any type of property.", false)
        ))
    )

    private val reactHooksQuestions = listOf(
        QuestionNode("What happens if you omit the dependency array in useEffect?", listOf(
            AnswerNode("The effect will run after every single render.", true),
            AnswerNode("It can cause performance issues if the effect performs heavy operations.", true),
            AnswerNode("If you update state inside that effect, it might cause an infinite loop.", true),
            AnswerNode("It is equivalent to passing an empty array.", false),
            AnswerNode("It is equivalent to not using useEffect at all.", false)
        )),
        QuestionNode("How do you clean up an effect in React?", listOf(
            AnswerNode("By returning a function from the useEffect callback.", true),
            AnswerNode("The cleanup function runs before the component unmounts.", true),
            AnswerNode("The cleanup function also runs before the effect re-runs (on next renders).", true),
            AnswerNode("It's used to unsubscribe from APIs or clear timers.", true),
            AnswerNode("React automatically cleans up all side effects, so this is rarely needed.", false)
        )),
        QuestionNode("Can you use async functions directly inside useEffect?", listOf(
            AnswerNode("No, useEffect expects a function that returns nothing or a cleanup function.", true),
            AnswerNode("Returning a Promise (which async functions do) is not allowed.", true),
            AnswerNode("You should define an async function inside the effect and call it immediately.", true),
            AnswerNode("Using an IIFE (Immediately Invoked Function Expression) is another way to use async code.", true),
            AnswerNode("Yes, React supports async useEffect since version 18.", false)
        )),
        QuestionNode("What is the difference between useLayoutEffect and useEffect?", listOf(
            AnswerNode("useEffect runs asynchronously after the browser has painted the screen.", true),
            AnswerNode("useLayoutEffect runs synchronously after all DOM mutations but before the browser paints.", true),
            AnswerNode("useLayoutEffect can be used to read DOM measurements and synchronously re-render.", true),
            AnswerNode("useEffect is preferred in most cases for better performance.", true),
            AnswerNode("There is no difference in their signature or usage.", false)
        )),
        QuestionNode("How do you handle race conditions in useEffect during data fetching?", listOf(
            AnswerNode("By using a boolean flag (e.g., 'ignore') inside the effect.", true),
            AnswerNode("Setting the flag to true in the cleanup function.", true),
            AnswerNode("Only updating state if the flag is still false after the fetch completes.", true),
            AnswerNode("Using AbortController to cancel the fetch request.", true),
            AnswerNode("React handles race conditions automatically for you.", false)
        ))
    )

    private val nextjsQuestions = listOf(
        QuestionNode("What are the benefits of React Server Components (RSC) in Next.js?", listOf(
            AnswerNode("Reduced bundle size because component code stays on the server.", true),
            AnswerNode("Direct access to backend resources (databases, file system).", true),
            AnswerNode("Better performance as the server sends HTML and data, not just JS.", true),
            AnswerNode("Improved SEO by providing fully rendered content to crawlers.", true),
            AnswerNode("They allow using React hooks like useState on the server.", false)
        )),
        QuestionNode("How do you share state between Client and Server Components?", listOf(
            AnswerNode("By passing state down as props from Server to Client components.", true),
            AnswerNode("By using query parameters in the URL.", true),
            AnswerNode("By using shared cookies or headers.", true),
            AnswerNode("By using a database or cache that both can access.", true),
            AnswerNode("By using React Context directly across both types.", false)
        )),
        QuestionNode("When should you use the 'use client' directive?", listOf(
            AnswerNode("When using React hooks (useState, useEffect, etc.).", true),
            AnswerNode("When using browser-only APIs (window, document, etc.).", true),
            AnswerNode("When adding event listeners (onClick, onChange).", true),
            AnswerNode("When using class components.", true),
            AnswerNode("For every component in the 'app' directory.", false)
        )),
        QuestionNode("How does SEO differ between Server and Client Components?", listOf(
            AnswerNode("Server Components provide pre-rendered HTML, which is better for all crawlers.", true),
            AnswerNode("Client Components rely on JS execution, which some crawlers might handle poorly.", true),
            AnswerNode("Metadata API in Next.js works best with Server Components.", true),
            AnswerNode("There is no difference if the crawler supports JavaScript.", false),
            AnswerNode("Client components are invisible to search engines.", false)
        )),
        QuestionNode("Can you use hooks in Server Components?", listOf(
            AnswerNode("No, hooks are only available in Client Components.", true),
            AnswerNode("Server Components are essentially async functions on the server.", true),
            AnswerNode("Using a hook in a Server Component will throw an error.", true),
            AnswerNode("You should refactor the component to a Client Component if you need hooks.", true),
            AnswerNode("Only 'useMemo' and 'useCallback' are allowed in Server Components.", false)
        ))
    )

    private val typescriptQuestions = listOf(
        QuestionNode("What is the difference between 'type' and 'interface' in TypeScript?", listOf(
            AnswerNode("Interfaces can be merged (declaration merging), while types cannot.", true),
            AnswerNode("Types can define unions, intersections, and primitives, while interfaces are mainly for objects.", true),
            AnswerNode("Interfaces are generally better for public APIs for extensibility.", true),
            AnswerNode("Types are better for complex transformations and utility types.", true),
            AnswerNode("Interfaces are only available at runtime, while types are removed.", false)
        )),
        QuestionNode("How do Mapped Types work in TypeScript?", listOf(
            AnswerNode("They allow creating new types based on the properties of an existing type.", true),
            AnswerNode("Syntax: { [P in K]: T }.", true),
            AnswerNode("Readonly<T> and Partial<T> are implemented using mapped types.", true),
            AnswerNode("You can use modifiers like '-' or '+' to add or remove 'readonly' or '?'.", true),
            AnswerNode("They are used to map values at runtime.", false)
        )),
        QuestionNode("What is the 'unknown' type and how is it different from 'any'?", listOf(
            AnswerNode("'unknown' is the type-safe counterpart of 'any'.", true),
            AnswerNode("You cannot perform any operations on a value of type 'unknown' without type checking.", true),
            AnswerNode("'any' allows you to do anything, essentially disabling type checking.", true),
            AnswerNode("'unknown' is the top type in TypeScript's type system.", true),
            AnswerNode("'unknown' can be assigned to any other type without casting.", false)
        )),
        QuestionNode("Explain Conditional Types with an example.", listOf(
            AnswerNode("They allow selecting one of two types based on a condition.", true),
            AnswerNode("Syntax: T extends U ? X : Y.", true),
            AnswerNode("They are often used for 'Exclude' and 'Extract' utility types.", true),
            AnswerNode("They enable very powerful type inference (using 'infer' keyword).", true),
            AnswerNode("They are evaluated at runtime using if/else.", false)
        )),
        QuestionNode("What are Template Literal Types?", listOf(
            AnswerNode("They allow using template literal syntax to create new string literal types.", true),
            AnswerNode("They can be combined with other types to produce all possible combinations.", true),
            AnswerNode("Example: type Color = 'red' | 'blue'; type Button = `\${Color}-button`;", true),
            AnswerNode("They are useful for strongly typing CSS classes or event names.", true),
            AnswerNode("They are used to format strings at runtime.", false)
        ))
    )

    private val microservicesQuestions = listOf(
        QuestionNode("Explain the SAGA pattern in microservices.", listOf(
            AnswerNode("It manages distributed transactions using a sequence of local transactions.", true),
            AnswerNode("Choreography: each service produces and listens to events to trigger the next step.", true),
            AnswerNode("Orchestration: a central controller tells participants which local transactions to execute.", true),
            AnswerNode("It uses compensating transactions to undo changes if a step fails.", true),
            AnswerNode("It guarantees ACID properties across all microservices.", false, "It guarantees Eventual Consistency, not immediate ACID.")
        )),
        QuestionNode("What is the Two-Phase Commit (2PC) protocol?", listOf(
            AnswerNode("It's a distributed algorithm to ensure all participants in a transaction either commit or rollback.", true),
            AnswerNode("Phase 1: Voting phase (coordinator asks participants if they are ready).", true),
            AnswerNode("Phase 2: Decision phase (coordinator tells everyone to commit or abort).", true),
            AnswerNode("It is a blocking protocol and can suffer from performance issues.", true),
            AnswerNode("It is the recommended way to handle transactions in all microservice architectures.", false)
        )),
        QuestionNode("How do you achieve eventual consistency in distributed systems?", listOf(
            AnswerNode("By using asynchronous messaging and events.", true),
            AnswerNode("By accepting that data might be temporarily inconsistent across services.", true),
            AnswerNode("By implementing idempotent consumers to handle duplicate messages.", true),
            AnswerNode("By using a 'read-your-writes' consistency model where possible.", true),
            AnswerNode("By locking all databases until the transaction is complete.", false)
        )),
        QuestionNode("What is the Outbox Pattern?", listOf(
            AnswerNode("It ensures that a database update and an event publication happen atomically.", true),
            AnswerNode("Events are first saved to an 'outbox' table in the same local transaction as the data change.", true),
            AnswerNode("A separate process (relay) polls the outbox and publishes messages to the broker.", true),
            AnswerNode("It avoids the 'dual write' problem.", true),
            AnswerNode("It is used to send emails from microservices.", false)
        )),
        QuestionNode("How do you handle failures in a choreography-based SAGA?", listOf(
            AnswerNode("By emitting 'failure' events that trigger compensating transactions in previous services.", true),
            AnswerNode("Each service must know how to 'undo' its local transaction.", true),
            AnswerNode("By using dead-letter queues for unprocessable messages.", true),
            AnswerNode("By implementing retries with exponential backoff.", true),
            AnswerNode("By stopping the entire system until manual intervention.", false)
        ))
    )
}
