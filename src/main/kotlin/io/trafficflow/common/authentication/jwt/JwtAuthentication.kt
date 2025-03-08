package io.trafficflow.common.authentication.jwt

import java.io.Serializable

data class JwtAuthentication(
    val token: String,

    val id: Long,
) : Serializable
