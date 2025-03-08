package io.trafficflow.configuration.redis

import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory

@Configuration
@Profile("local")
class RedisLocalConfiguration(
    private val redisProperties: RedisProperties,
) {
    @Bean
    fun redisConnectionsFactory() = LettuceConnectionFactory(redisProperties.host, redisProperties.port)
}
