package io.trafficflow.configuration.security

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
class JwtConfiguration(
    val issuer: String,

    val secret: String,

    val accessTokenExpireMinute: Int,

    val refreshTokenExpireMinute: Int,

    val domain: String,
)
