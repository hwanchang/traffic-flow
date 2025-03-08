package io.trafficflow.partnerconnector.client.data

import io.trafficflow.partnerconnector.domain.User

class UserFeignResponse(
    val id: Long,

    val email: String,

    val nickname: String,
) {
    fun toDomain() = User(email = email)
}
