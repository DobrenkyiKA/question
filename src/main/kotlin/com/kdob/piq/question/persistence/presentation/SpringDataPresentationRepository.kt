package com.kdob.piq.question.persistence.presentation

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SpringDataPresentationRepository : JpaRepository<PresentationEntity, UUID>