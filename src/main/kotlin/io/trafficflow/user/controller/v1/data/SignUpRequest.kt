package io.trafficflow.user.controller.v1.data

import io.trafficflow.user.domain.User

data class SignUpRequest(
    val email: String,

    val password: String,

    val nickname: String,
) {
    fun toDomain() = User(
        email = email,
        password = password,
        nickname = nickname,
    )
}
