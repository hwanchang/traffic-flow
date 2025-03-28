package io.trafficflow.partnerconnector.service

import io.trafficflow.configuration.kafka.KafkaMessage
import java.lang.System.currentTimeMillis
import java.util.concurrent.TimeUnit.SECONDS
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class RateLimiter(
    private val rateLimiterRedisTemplate: RedisTemplate<String, Int>,

    @Value("\${kafka.rate-limiter.count}")
    private val rateLimiterCount: Long,
) {
    fun isLimited(kafkaMessage: KafkaMessage): Boolean {
        val key = "traffic-flow:rate-limit:${currentTimeMillis() / 1000}"
        val count = rateLimiterRedisTemplate.opsForValue().increment(key) ?: 0
        rateLimiterRedisTemplate.expire(key, 5, SECONDS)

        return count > rateLimiterCount
    }
}
