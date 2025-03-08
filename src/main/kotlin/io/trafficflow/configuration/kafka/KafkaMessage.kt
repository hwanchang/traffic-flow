package io.trafficflow.configuration.kafka

import io.trafficflow.common.event.EventFailover
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.Serializable
import java.time.LocalDateTime
import java.time.LocalDateTime.now

@JsonSerialize
data class KafkaMessage(
    val body: Any,

    val failoverCount: Int = 0,

    val lastRetriedAt: LocalDateTime? = null,

    val createdAt: LocalDateTime = now(),
) : Serializable {
    constructor(eventFailover: EventFailover) : this(
        body = eventFailover.message,
        failoverCount = eventFailover.failoverCount + 1,
        lastRetriedAt = now(),
        createdAt = eventFailover.createdAt,
    )

    inline fun <reified T> asBody(): T =
        jacksonObjectMapper().registerModules(JavaTimeModule()).convertValue(body, T::class.java)
}
