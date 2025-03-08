package io.trafficflow.common.event

import io.trafficflow.configuration.kafka.KafkaMessage
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class EventFailoverHandler(
    private val eventFailoverRedisTemplate: RedisTemplate<String, EventFailover>,

    private val kafkaMessageKafkaTemplate: KafkaTemplate<String, KafkaMessage>,
) {
    fun add(eventFailover: EventFailover) {
        eventFailoverRedisTemplate.opsForList()
            .leftPushAll(EVENT_FAILOVER_KEY, eventFailover)
    }

    fun retry() {
        eventFailoverRedisTemplate.opsForList()
            .range(EVENT_FAILOVER_KEY, 0, -1)
            .apply { eventFailoverRedisTemplate.delete(EVENT_FAILOVER_KEY) }
            ?.forEach { kafkaMessageKafkaTemplate.send(it.topic, KafkaMessage(eventFailover = it)) }
    }

    companion object {
        private const val EVENT_FAILOVER_KEY = "event:failover"
    }
}
