package io.trafficflow.user.controller.v1.data

import io.trafficflow.user.domain.User

class UserResponse(user: User) {
    val id: Long = user.id

    val email: String = user.email

    val nickname: String = user.nickname
}
