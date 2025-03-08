package io.trafficflow.common.domain

import kotlinx.serialization.Serializable

@Serializable
class Token(
    val accessToken: String,

    val refreshToken: String,
)
