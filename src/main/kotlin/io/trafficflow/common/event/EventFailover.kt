package io.trafficflow.common.event

import java.time.LocalDateTime

data class EventFailover(
    val topic: String,

    val message: Any,

    val exceptionMessage: String? = null,

    val exceptionStacktrace: String? = null,

    val failoverCount: Int,

    val lastRetriedAt: LocalDateTime? = null,

    val createdAt: LocalDateTime,
)
