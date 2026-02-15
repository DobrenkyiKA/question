package com.kdob.piq.question.api.security

import org.springframework.security.core.context.SecurityContextHolder
import java.util.UUID

fun currentUserId(): UUID =
    UUID.fromString(
        SecurityContextHolder
            .getContext()
            .authentication
            ?.name
    )