package com.szymanski.sharelibrary.core.exception

import java.time.LocalDateTime

data class ExceptionErrorBody(
    private val timestamp: LocalDateTime?,
    private val message: String?,
)