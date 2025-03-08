package io.trafficflow.auth.controller.v1.data

import io.trafficflow.auth.domain.login.Login

data class LoginRequest(
    val email: String,

    val password: String,
) {
    fun toDomain() = Login(
        email = email,
        password = password,
    )
}
