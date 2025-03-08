package io.trafficflow.user.domain

enum class Role(
    val role: String,
) {
    ADMIN("ROLE_ADMIN"),

    USER("ROLE_USER");

    companion object {
        fun from(role: String) = entries.first { it.role == role }
    }
}
