package io.trafficflow.configuration.redis

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.trafficflow.common.event.EventFailover
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfiguration(
    private val redisConnectionsFactory: LettuceConnectionFactory,
) {
    @Bean
    fun rateLimiterRedisTemplate() = RedisTemplate<String, Int>().apply {
        connectionFactory = redisConnectionsFactory
        keySerializer = StringRedisSerializer()
        valueSerializer = StringRedisSerializer()
    }

    @Bean
    fun eventFailoverRedisTemplate() = RedisTemplate<String, EventFailover>().apply {
        connectionFactory = redisConnectionsFactory
        keySerializer = StringRedisSerializer()
        valueSerializer = Jackson2JsonRedisSerializer(
            jacksonObjectMapper().registerModule(JavaTimeModule()),
            EventFailover::class.java,
        )
    }
}
