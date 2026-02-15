package com.kdob.piq.question.persistence.progress

import org.springframework.data.jpa.repository.JpaRepository

interface SpringDataLearningProgressRepository :
    JpaRepository<LearningProgressEntity, LearningProgressId>